#!/bin/bash

# Configuration des dossiers
DOCS_SRC="docs"
OUTPUT_DIR="doc"
CSS_URL="https://cdnjs.cloudflare.com/ajax/libs/github-markdown-css/5.2.0/github-markdown.min.css"

# Créer le dossier de sortie s'il n'existe pas
mkdir -p "$OUTPUT_DIR"

# 1. Télécharger une feuille de style "GitHub Markdown" pour le rendu
curl -s "$CSS_URL" > "$OUTPUT_DIR/markdown.css"

# Petit enrobage HTML pour que le CSS GitHub s'applique correctement
HEADER="<div class='markdown-body' style='padding: 45px; max-width: 980px; margin: 0 auto;'>"
FOOTER="</div>"

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

# 3. Convertir tous les fichiers dans docs/ vers doc/
if [ -d "$DOCS_SRC" ]; then
    for f in "$DOCS_SRC"/*.md; do
        [ -e "$f" ] || continue
        filename=$(basename "$f" .md)
        echo "Traitement de $filename..."

        pandoc "$f" -s \
            --metadata title="EcoDrop - $filename" \
            --css="markdown.css" \
            --include-before-body=<(echo "$HEADER") \
            --include-after-body=<(echo "$FOOTER") \
            -o "$OUTPUT_DIR/$filename.html"
    done
else
    echo "Dossier $DOCS_SRC non trouvé."
fi

# 4. Correction des liens (Remplace .md par .html dans les fichiers générés)
if ls "$OUTPUT_DIR"/*.html >/dev/null 2>&1; then
    sed -i 's/href="docs\/\([^"]*\)\.md"/href="\1.html"/g' "$OUTPUT_DIR"/*.html 2>/dev/null
    sed -i 's/href="\([^"]*\)\.md"/href="\1.html"/g' "$OUTPUT_DIR"/*.html 2>/dev/null
    sed -i 's/href="README\.html"/href="index.html"/g' "$OUTPUT_DIR"/*.html 2>/dev/null
    sed -i 's/href="\.\.\/README\.html"/href="index.html"/g' "$OUTPUT_DIR"/*.html 2>/dev/null
fi

echo "Terminé ! La doc est disponible dans le dossier /doc"