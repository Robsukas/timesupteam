# Time's Up Team

Projekti kirjeldust ja nädalate plaani vaata: [Wiki | Üldine plaan](https://gitlab.cs.ttu.ee/heohak/iti0301-2023/-/wikis/%C3%9Cldine-plaan)

# Set-up hindajatele (lokaalne)

1. `git clone https://gitlab.cs.ttu.ee/heohak/iti0301-2023.git`
2. Pane käima lokaalne server `MainServer.java` (`server/src/main/java/com/rahamehed/server/MainServer.java`)
3. Pane käima 2x lokaalne klient `DesktopLauncher.java` (`client/desktop/src/com/timesupteam/DesktopLauncher.java`)
4. Naudi 😎

Remote ühenduseks saamiseks tuleb Gradle'iga genereerida serverist .jar fail.
server > Tasks > build > build
`java -jar server/build/libs/server-1.0.jar` (kopeerida .jar nt TalTechi serverisse ja seal käima panna)
Ja ära unusta... nautida 😎
