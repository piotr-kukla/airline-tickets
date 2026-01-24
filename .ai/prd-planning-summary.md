<conversation_summary>
<decisions>
MVP monitoruje ceny lotów Ryanair i wysyła e-mail, gdy cena RT spadnie poniżej limitu użytkownika.
W MVP obsługujemy tylko loty bezpośrednie i tylko z jednego lotniska wylotu skonfigurowanego w systemie (np. Wrocław). Użytkownik wybiera trasy wyłącznie z tego lotniska.
Kierunek docelowy jest wybierany jako konkretny port/lotnisko (IATA), 1 miasto = 1 port (brak wyboru „miasta” z wieloma lotniskami).
Użytkownik definiuje okno dat wspólne: oba loty (wylot i powrót) muszą mieścić się w przedziale [startDate, endDate].
Użytkownik definiuje długość pobytu jako min/max dni (od razu min/max), z ograniczeniem: różnica max 5 dni.
Maksymalny rozmiar okna dat w MVP to 1 miesiąc.
Cena limitu dotyczy 1 pasażera i obejmuje tylko bilet (bez dodatków). Waluta zawsze PLN.
Kryterium alertu to suma RT: wylot + powrót (nie osobno).
System ma znaleźć wszystkie pary dat (wylot, powrót) w oknie, spełniające warunek; w e-mailu wysyła 3 najtańsze opcje oraz liczbę wszystkich opcji.
Alertowanie jest codzienne i „bez pamięci”: jeśli w danym dniu cena jest poniżej limitu, wysyłamy e-mail; następnego dnia historia nie jest brana pod uwagę (jeśli nadal poniżej limitu, wysyłamy ponownie).
Job uruchamiany raz dziennie o stałej porze; strefa czasowa Europe/Warsaw; po zakończeniu sprawdzania wysyłane są e-maile.
Limit konfiguracji: maksymalnie 5 alertów na użytkownika.
W MVP liczymy cenę RT dla pary dat jako: min(cena wylotu w dacie) + min(cena powrotu w dacie) i w komunikacji oznaczamy to jako „od”.
Redukcja zapytań: w ramach jednego dnia nie odpytujemy Ryanair ponownie o cenę tego samego lotu; klucz deduplikacji opiera się o (origin, destination, date), a rozróżnienie lotów tego samego dnia uwzględnia numer rejsu (bo może być wiele lotów w dacie).
Auth ma być „klasyczny” i powszechny: logowanie hasłem, reset hasła, oraz wymagana weryfikacja e-mail.
Obsługa błędów: jeśli nie uda się sprawdzić, wysyłamy jeden zbiorczy e-mail dziennie per użytkownik z powodem.
Audyt cen: zapisujemy tylko rekordy gdy oferta istnieje; pola: cena, data lotu, miasto wylotu, miasto przylotu, data sprawdzania.
Audyt cen nie wymaga powiązania z użytkownikiem; użytkownik może usunąć konto/alerty; przy usunięciu konta usuwamy powiązania, ale same rekordy cen pozostają w bazie.
Dni tygodnia jako filtr są poza MVP.
</decisions>
<matched_recommendations>
Jednoznacznie zdefiniować model wyszukiwania: wspólne okno dat + min/max długość pobytu oraz warunek, że oba loty mieszczą się w oknie (zapobiega różnym interpretacjom).
Ustandaryzować definicję ceny i kryterium porównania: limit w PLN, cena za 1 pasażera, tylko bilet, porównanie po sumie RT; w mailu pokazywać sumę i składowe oraz oznaczenie „od”.
Zdefiniować zasady alertowania i deduplikacji komunikacji: codzienny alert bez pamięci + raport zbiorczy per użytkownik; ograniczać „spam” przez agregację (1 mail/dzień) i top 3 wyników.
Wprowadzić limity MVP ograniczające złożoność: max 1 miesiąc okna, różnica pobytu ≤ 5 dni, max 5 alertów/użytkownik.
Sformalizować deduplikację zapytań do Ryanair na poziomie pojedynczego lotu oraz wyjaśnić rolę numeru rejsu (wiele lotów jednego dnia) i sposób wyboru „minimum” do liczenia RT.
Określić minimalny zestaw informacji w e-mailach: dla wyników (cena, daty, lotnisko wylotu i przylotu) oraz dla błędów (zbiorczo + powód).
Spisać wymagania dot. auth: klasyczne logowanie + reset + obowiązkowa weryfikacja e-mail przed pełnym korzystaniem (np. aktywacją alertów/wysyłką).
Opisać zasady audytu cen: jakie pola zapisujemy, kiedy zapisujemy (tylko gdy oferta istnieje), oraz jak usuwamy powiązania przy kasowaniu konta (audit danych cenowych pozostaje).
</matched_recommendations>
<prd_planning_summary>
Główne wymagania funkcjonalne produktu
Konta użytkowników:
rejestracja/logowanie hasłem
reset hasła
obowiązkowa weryfikacja e-mail
zarządzanie alertami (dodaj/edytuj/usuń), limit 5 alertów na użytkownika
możliwość usunięcia konta (usuwa powiązania, nie usuwa historycznych rekordów cen)
Konfiguracja alertu (dla jednego docelowego portu IATA):
origin: jedno lotnisko systemowe (np. Wrocław)
destination: konkretne lotnisko (IATA)
okno dat [startDate, endDate] (oba loty muszą się mieścić w oknie)
długość pobytu: minDays/maxDays (max różnica 5 dni)
waluta: PLN
limit ceny: za 1 pasażera, tylko bilet
Dzienne sprawdzanie cen:
job uruchamiany raz dziennie o stałej porze (Europe/Warsaw)
wyliczanie wszystkich par dat (wylot, powrót) spełniających ograniczenia i warunek ceny
cena RT dla pary dat liczona jako: min(wylot w dacie) + min(powrót w dacie) i prezentowana jako „od”
redukcja zapytań do Ryanair: w ramach dnia nie pobieramy ponownie ceny tego samego lotu; uwzględniamy numer rejsu (wiele lotów w dacie)
Powiadomienia e-mail:
jeśli znaleziono opcje poniżej limitu: wysyłamy e-mail (codziennie, bez deduplikacji między dniami)
treść: cena, daty, lotnisko wylotu/przylotu; top 3 najtańsze + liczba wszystkich opcji
gdy nie uda się sprawdzić: jeden zbiorczy e-mail per użytkownik/dzień z powodem
Audyt cen:
zapisujemy tylko gdy oferta istnieje
pola: cena, data lotu, miasto wylotu, miasto przylotu, data sprawdzania
dane cenowe pozostają niezależnie od konta; powiązania z kontem mogą być usuwane
Kluczowe historie użytkownika i ścieżki korzystania
Rejestracja i aktywacja:
jako użytkownik rejestruję konto, potwierdzam e-mail i loguję się, aby móc skonfigurować alerty
Konfiguracja alertu:
jako użytkownik dodaję alert dla trasy z Wrocławia do wskazanego portu IATA, ustawiam okno dat, min/max długość pobytu oraz limit ceny w PLN
jako użytkownik mogę mieć do 5 aktywnych alertów
Otrzymanie powiadomienia:
jako użytkownik raz dziennie otrzymuję mail z wynikami, jeśli istnieją pary dat spełniające limit (top 3 + liczba wszystkich)
jeśli system nie zdołał sprawdzić, otrzymuję zbiorczy mail z powodem
Zarządzanie kontem:
jako użytkownik mogę usuwać alerty lub konto; po usunięciu konta moje powiązania z danymi są usuwane, ale audyt cen pozostaje
Ważne kryteria sukcesu i sposoby ich mierzenia
Skuteczność wykrywania okazji:
liczba (i odsetek) dni, w których system poprawnie znajduje pary RT poniżej limitu dla aktywnych alertów
Skuteczność powiadomień:
odsetek dni, w których e-maile zostały wysłane (sukces wysyłki) vs. liczba e-maili „nie udało się sprawdzić”
Użyteczność wyników:
ile alertów generuje co najmniej 1 opcję w zadanym okresie; oraz średnia liczba opcji na alert (przy zachowaniu limitów okna/pobytu)
Stabilność integracji:
liczba błędów sprawdzania per dzień i ich przyczyny (na podstawie raportu błędów wysyłanego użytkownikom i logów systemowych)
Nierozwiązane kwestie / obszary do doprecyzowania
Mimo ustalenia dodania numeru rejsu, do doprecyzowania pozostaje model danych i integracji: czy audyt i deduplikacja działają na poziomie lotu per flightNumber, czy pobieramy listę lotów dla (origin,destination,date) i wybieramy minimum (to wpływa na projekt API i bazę).
Sposób prezentacji wyników w mailu (format, czy podajemy też godziny lotów „informacyjnie” mimo że logika jest tylko po datach) nie został jednoznacznie ustalony.
Szczegóły procesu weryfikacji e-mail (ważność tokenu, ponowne wysłanie, zachowanie przy braku weryfikacji) nie zostały rozpisane, choć wymaganie weryfikacji jest potwierdzone.
</prd_planning_summary>
<unresolved_issues>
Dokładna definicja „jednego odpytania” Ryanair oraz rola numeru rejsu w deduplikacji: per flightNumber vs. per (origin,destination,date) z listą lotów.
Zakres audytu cen przy wielu lotach jednego dnia: zapis per flightNumber czy tylko minimalna cena/dzień (spójność z decyzją o dodaniu nr rejsu).
Szczegóły weryfikacji e-mail: TTL tokenu, resend, blokady funkcji przed weryfikacją.
Format e-maila wynikowego: czy poza ceną/daty/lotniska pokazujemy też dodatkowe pola (np. godziny) wyłącznie informacyjnie.
</unresolved_issues>
</conversation_summary>