# --- STAGE 1: The Ninja Builder ---
FROM tomcat:10.1-jdk21 AS builder

WORKDIR /build
# Copy your raw repository code into the container
COPY . .

# Create the folders Tomcat requires
RUN mkdir -p WEB-INF/lib WEB-INF/classes

# Download all 9 dependencies directly from Maven Central into the lib folder
RUN wget -q -O WEB-INF/lib/postgresql-42.7.8.jar https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.8/postgresql-42.7.8.jar
RUN wget -q -O WEB-INF/lib/jackson-annotations-2.15.3.jar https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.15.3/jackson-annotations-2.15.3.jar
RUN wget -q -O WEB-INF/lib/jackson-core-2.15.3.jar https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.15.3/jackson-core-2.15.3.jar
RUN wget -q -O WEB-INF/lib/jackson-databind-2.15.3.jar https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.15.3/jackson-databind-2.15.3.jar
RUN wget -q -O WEB-INF/lib/jackson-dataformat-xml-2.15.3.jar https://repo1.maven.org/maven2/com/fasterxml/jackson/dataformat/jackson-dataformat-xml/2.15.3/jackson-dataformat-xml-2.15.3.jar
RUN wget -q -O WEB-INF/lib/jjwt-api-0.12.5.jar https://repo1.maven.org/maven2/io/jsonwebtoken/jjwt-api/0.12.5/jjwt-api-0.12.5.jar
RUN wget -q -O WEB-INF/lib/jjwt-impl-0.12.5.jar https://repo1.maven.org/maven2/io/jsonwebtoken/jjwt-impl/0.12.5/jjwt-impl-0.12.5.jar
RUN wget -q -O WEB-INF/lib/jjwt-jackson-0.12.5.jar https://repo1.maven.org/maven2/io/jsonwebtoken/jjwt-jackson/0.12.5/jjwt-jackson-0.12.5.jar
RUN wget -q -O WEB-INF/lib/stax2-api-4.2.2.jar https://repo1.maven.org/maven2/org/codehaus/woodstox/stax2-api/4.2.2/stax2-api-4.2.2.jar

# Compile all .java files using Tomcat's built-in jars and our newly downloaded jars
RUN find WEB-INF/src -name "*.java" > sources.txt
RUN javac -cp "/usr/local/tomcat/lib/*:WEB-INF/lib/*" -d WEB-INF/classes @sources.txt

# Crucial: Move config.prop into the classes folder so your DS.java can find it
RUN cp WEB-INF/res/* WEB-INF/classes/


# --- STAGE 2: The Clean Runtime ---
FROM tomcat:10.1-jdk21

# Delete the default Tomcat dummy apps
RUN rm -rf /usr/local/tomcat/webapps/*
RUN mkdir -p /usr/local/tomcat/webapps/ROOT

# Copy the fully compiled project from the builder stage
COPY --from=builder /build/ /usr/local/tomcat/webapps/ROOT/

EXPOSE 8080
CMD ["catalina.sh", "run"]