# ZomatoX v1 (MVP)

## Backend Build Troubleshooting (JDK/Lombok)

Root cause: Maven is picking JDK 25 on your machine, and Lombok + javac internals are crashing (`TypeTag::UNKNOWN`).

I patched `portfolio/zomatox-v1/backend/pom.xml` to:

- pin compiler plugin config to `release 17`
- upgrade Lombok wiring (`${lombok.version}`) for better new-JDK compatibility
- add an enforcer rule so build fails early with a clear message unless Java is 17/21

Now run backend with JDK 17 (or 21) explicitly:

```bash
cd /Users/meghnadsaha/IdeaProjects/angular-typescript-starter/portfolio/zomatox-v1/backend
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
mvn clean package
```

If you prefer Java 21:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn clean package
```

If it still fails, send me the new error block and I’ll patch further.


  - pin compiler plugin config to release 17


```bash
› Root cause: Maven is picking JDK 25 on your machine, and Lombok + javac internals are crashing (TypeTag::UNKNOWN).

    I patched portfolio/zomatox-v1/backend/pom.xml to:

    - pin compiler plugin config to release 17
    - upgrade Lombok wiring (${lombok.version}) for better new-JDK compatibility
    Now run backend with JDK 17 (or 21) explicitly:

    cd /Users/meghnadsaha/IdeaProjects/angular-typescript-starter/portfolio/zomatox-v1/backend
    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
    mvn clean package

    If you prefer Java 21:

    export JAVA_HOME=$(/usr/libexec/java_home -v 21)
    mvn clean package

    If it still fails, send me the new error block and I’ll patch further. add this on md


    1. Restart backend (mvn spring-boot:run).
    2. Reload frontend and test again.


  cd /Users/meghnadsaha/IdeaProjects/angular-typescript-starter/portfolio/zomatox-v1/backend
  export JAVA_HOME=$(/usr/libexec/java_home -v 17)
  mvn spring-boot:run

  Then:

  - If X-User-Id: 1, use address 1 or 2
  - If X-User-Id: 2, use address 3 or 4
    
    ```