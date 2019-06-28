

Grupp 04

Wilhelm Ericsson

Ruben vilhelmsen

Koden är skriven i Java och kräver Processings core.jar biblioteket för att köras. För att testa systemet kan man köra den medföljda byggda jar filen (Inlupp_AI_3.jar)
av lösningen eller skapa ett projekt av källkoden i en IDE. Vi själva har använt IntelliJ IDEA för att köra programmet och mappen "Inlupp_AI_3" i den medföljda zip-filen
går att öppna med IntelliJ. Ifall man vill skapa ett eget projekt med filerna så behöver man alltså bara lägga in alla klasser samt bilderna på kartorna påsamma
ställe (de sparade Q-tabellerna är inget krav, utan tillhandahåller endast värden från en tränad agent). Klasserna är alltså inte del av något package. Efter det behöver man
bara lägga till core.jar som ett bibliotek till projektet.

När man kör programmet så kan man nyttja följande kommandon:
    'd' - visa/visa inte rutnätet.
    's' - spara agentens framsteg, alltså dess nuvarande Q-tabell - detta skriver över den tidigare sparade Q-tabell.
    'r' - återställer agenten, alltså börjar om lärandeprocessen från början.
    'R' - (shift-r) Återställer agenten från en sparad Q-tabellsfil.
    't' - Försätter agenten i testläge.
    'T' - (shift-t) Försätter agenten i träningsläge.
    '+' - Ökar programmets frame rate med 10.
    '-' - Minskar programmets frame rate med 10.
    '1' - Byter till karta 1 och återställer agenten (om en redan tränad agenten önskas och en sådan finns använd kommandot 'R' efter bytet).
    '2' - Byter till karta 2 och återställer agenten (om en redan tränad agenten önskas och en sådan finns använd kommandot 'R' efter bytet).
    '3' - Byter till karta 3 och återställer agenten (om en redan tränad agenten önskas och en sådan finns använd kommandot 'R' efter bytet).



