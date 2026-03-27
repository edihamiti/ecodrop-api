#!/bin/bash

# Configuration des dossiers
DOCS_SRC="docs"
OUTPUT_DIR="docs"
CSS_URL="https://cdnjs.cloudflare.com/ajax/libs/github-markdown-css/5.2.0/github-markdown.min.css"

# Créer le dossier de sortie s'il n'existe pas
mkdir -p "$OUTPUT_DIR"

# 1. Télécharger une feuille de style "GitHub Markdown" pour le rendu
curl -s "$CSS_URL" > "$OUTPUT_DIR/markdown.css"

# Petit enrobage HTML pour que le CSS GitHub s'applique correctement
HEADER="<div class='markdown-body' style='padding: 45px; max-width: 980px; margin: 0 auto;'>"
FOOTER="</div>
<script src='https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.min.js'></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // On cherche les blocs <pre class='mermaid'> générés par pandoc
        const mermaidBlocks = document.querySelectorAll('pre.mermaid');
        mermaidBlocks.forEach(block => {
            const code = block.querySelector('code');
            const content = code ? code.textContent : block.textContent;
            
            const div = document.createElement('div');
            div.className = 'mermaid';
            div.textContent = content;
            
            block.parentNode.replaceChild(div, block);
        });
        mermaid.initialize({ startOnLoad: true, theme: 'default' });
    });
</script>"

echo "Génération de la documentation..."

# 2. Convertir le README.md racine en index.html (la page d'accueil)
if [ -f "README.md" ]; then
    pandoc README.md -s \
        --metadata title="EcoDrop - Documentation" \
        --css="markdown.css" \
        --include-before-body=<(echo "$HEADER") \
        --include-after-body=<(echo "$FOOTER") \
        -o "$OUTPUT_DIR/index.html"
else
    echo "README.md non trouvé à la racine."
fi

# 3. Convertir tous les fichiers dans docs/ vers doc/ (récursivement)
if [ -d "$DOCS_SRC" ]; then
    find "$DOCS_SRC" -name "*.md" | while read -r f; do
        # Chemin relatif par rapport à DOCS_SRC (ex: mathieu/users.md)
        rel_path="${f#$DOCS_SRC/}"
        
        # Dossier de destination
        dest_dir="$OUTPUT_DIR/$(dirname "$rel_path")"
        mkdir -p "$dest_dir"
        
        # Nom du fichier sans extension
        filename=$(basename "$f" .md)
        # Chemin du fichier de sortie
        out_file="$OUTPUT_DIR/${rel_path%.md}.html"
        
        echo "Traitement de $rel_path..."

        # Calcul de la profondeur pour le chemin du CSS
        depth=$(echo "$rel_path" | tr -cd '/' | wc -c)
        css_path="markdown.css"
        for ((i=0; i<depth; i++)); do
            css_path="../$css_path"
        done

        pandoc "$f" -s \
            --metadata title="EcoDrop - $filename" \
            --css="$css_path" \
            --include-before-body=<(echo "$HEADER") \
            --include-after-body=<(echo "$FOOTER") \
            -o "$out_file"
    done
else
    echo "Dossier $DOCS_SRC non trouvé."
fi

# 4. Correction des liens
if find "$OUTPUT_DIR" -name "*.html" | grep -q .; then
    # 1. Remplace .md par .html pour les liens simples
    find "$OUTPUT_DIR" -name "*.html" -exec sed -i.bak 's/href="\([^"]*\)\.md"/href="\1.html"/g' {} +

    # 2. Remplace .md par .html pour les liens contenant une ancre (ex: #wastetype)
    find "$OUTPUT_DIR" -name "*.html" -exec sed -i.bak 's/href="\([^"]*\)\.md#\([^"]*\)"/href="\1.html#\2"/g' {} +

    # 3. Supprime les préfixes /docs/ et docs/ car on veut des liens relatifs
    find "$OUTPUT_DIR" -name "*.html" -exec sed -i.bak 's/href="\/docs\//href="/g' {} +
    find "$OUTPUT_DIR" -name "*.html" -exec sed -i.bak 's/href="docs\//href="/g' {} +

    # 4. Redirige les liens README vers la nouvelle page d'accueil index.html
    find "$OUTPUT_DIR" -name "*.html" -exec sed -i.bak 's/href="\([^"]*\)README\.html/href="\1index.html/g' {} +

    # 5. Nettoyage silencieux des fichiers de sauvegarde créés par sed
    find "$OUTPUT_DIR" -name "*.bak" -delete
fi

echo "Terminé ! La doc est disponible dans le dossier /"$OUTPUT_DIR". Ouvrez index.html pour commencer."