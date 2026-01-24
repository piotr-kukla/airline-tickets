# Dokument wymagań produktu (PRD) - airline-tickets
## 1. Przegląd produktu
### 1.1 Cel produktu
airline-tickets to aplikacja, która automatycznie (raz dziennie) monitoruje ceny lotów Ryanair dla zdefiniowanych przez użytkownika alertów i wysyła e-mail, gdy cena przelotu w obie strony (RT) spadnie poniżej ustawionego limitu.

### 1.2 Zakres MVP (najważniejsze założenia)
1. Monitorujemy wyłącznie loty Ryanair.
2. W MVP obsługujemy tylko loty bezpośrednie.
3. W MVP obsługujemy tylko jedno lotnisko wylotu skonfigurowane po stronie systemu (origin), np. Wrocław (WRO). Użytkownik nie wybiera lotniska wylotu.
4. Kierunek docelowy (destination) użytkownik wybiera jako konkretne lotnisko/port z kodem IATA (1 miasto = 1 port w MVP).
5. Użytkownik definiuje jedno wspólne okno dat [startDate, endDate]; zarówno wylot, jak i powrót muszą mieścić się w tym oknie.
6. Użytkownik definiuje długość pobytu jako zakres minDays/maxDays (od razu oba), z ograniczeniem maxDays - minDays ≤ 5.
7. Maksymalny rozmiar okna dat w MVP to 1 miesiąc (tj. endDate - startDate ≤ 31 dni kalendarzowych).
8. Limit ceny jest w PLN, dotyczy 1 pasażera i obejmuje wyłącznie cenę biletu (bez dodatków). Waluta jest stała: PLN.
9. Kryterium alertu to suma RT: cena wylotu + cena powrotu (nie porównujemy osobno).
10. Alertowanie jest codzienne i bez pamięci (brak deduplikacji między dniami): jeśli danego dnia są opcje poniżej limitu, wysyłamy e-mail; jeśli następnego dnia nadal poniżej limitu, wysyłamy ponownie.
11. Harmonogram sprawdzania: jeden job raz dziennie o stałej porze w strefie czasowej Europe/Warsaw; e-maile są wysyłane po zakończeniu całego sprawdzania.
12. Limit alertów: maksymalnie 5 aktywnych alertów na użytkownika.

### 1.3 Użytkownicy i interesariusze
1. Użytkownik końcowy: osoba, która chce znaleźć tani lot i nie chce ręcznie sprawdzać cen codziennie.
2. Operator systemu (wewnętrzny): konfiguruje origin, harmonogram, integrację e-mail, oraz monitoruje poprawność joba.

### 1.4 Definicje i pojęcia
1. Alert: zapisane kryteria monitorowania ceny RT dla trasy origin -> destination oraz ograniczeń dat i pobytu.
2. Okno dat: przedział [startDate, endDate] wspólny dla wylotu i powrotu.
3. Para dat: (departureDate, returnDate) spełniająca warunki okna dat i długości pobytu.
4. Cena RT (w MVP): min(cena wylotu w departureDate) + min(cena powrotu w returnDate), liczona na podstawie listy lotów w danej dacie. W komunikacji e-mail oznaczana jako cena od.
5. Oferta istnieje: dla danego lotu (lub lotów) w danej dacie dostępna jest cena biletu (base fare) w PLN.

## 2. Problem użytkownika
### 2.1 Opis problemu
Ceny biletów Ryanair często się zmieniają. Aby znaleźć korzystną cenę, użytkownik musiałby codziennie ręcznie sprawdzać różne kierunki i kombinacje dat na stronie Ryanair, co jest czasochłonne i łatwe do przeoczenia.

### 2.2 Jak produkt rozwiązuje problem
1. Użytkownik definiuje alert (kierunek, okno dat, zakres długości pobytu, limit ceny w PLN).
2. System raz dziennie automatycznie sprawdza dostępne ceny w oknie dat.
3. Jeśli system znajdzie pary dat spełniające limit ceny RT, wysyła e-mail z podsumowaniem (top 3 najtańsze opcje i liczba wszystkich opcji).
4. Jeśli sprawdzenie nie powiedzie się, użytkownik dostaje zbiorczy e-mail z informacją o błędzie i powodem.

### 2.3 Kontekst i ograniczenia MVP
1. Tylko loty bezpośrednie.
2. Tylko jeden origin skonfigurowany w systemie.
3. Sprawdzanie raz dziennie o stałej porze (brak sprawdzania w ciągu dnia).

## 3. Wymagania funkcjonalne
### 3.1 Konta użytkowników i dostęp
1. Rejestracja konta użytkownika:
   - użytkownik podaje co najmniej e-mail i hasło,
   - po rejestracji konto wymaga weryfikacji e-mail.
2. Weryfikacja e-mail:
   - system wysyła e-mail z linkiem weryfikacyjnym po rejestracji,
   - system umożliwia ponowne wysłanie linku weryfikacyjnego,
   - token weryfikacyjny ma ograniczony czas ważności (MVP: 24h),
   - do czasu weryfikacji e-mail użytkownik może się zalogować, ale nie może aktywować alertów ani otrzymywać alertów cenowych.
3. Logowanie:
   - logowanie klasyczne: e-mail + hasło,
   - po poprawnym logowaniu użytkownik uzyskuje dostęp do panelu alertów.
4. Reset hasła:
   - użytkownik może zainicjować reset hasła dla e-mail,
   - system wysyła e-mail z linkiem resetu,
   - token resetu ma ograniczony czas ważności (MVP: 1h),
   - po ustawieniu nowego hasła poprzedni token resetu staje się nieważny.
5. Podstawowe wymagania bezpieczeństwa dostępu:
   - hasła są przechowywane wyłącznie w postaci bezpiecznych hashy (bez przechowywania haseł jawnie),
   - system posiada ograniczenie liczby nieudanych prób logowania (rate limiting) na e-mail i/lub IP,
   - linki weryfikacyjne i resetu są jednorazowe (po użyciu wygasają).

### 3.2 Zarządzanie alertami (CRUD) i walidacje
1. Lista alertów:
   - użytkownik widzi listę swoich alertów oraz ich status (aktywny/nieaktywny),
   - użytkownik widzi ostatni wynik sprawdzania dla alertu (np. ostatnia data sprawdzenia i informacja: znaleziono oferty / brak ofert / błąd).
2. Dodanie alertu:
   - użytkownik może dodać alert, jeżeli ma zweryfikowany e-mail,
   - limit: maksymalnie 5 aktywnych alertów na użytkownika,
   - pola alertu:
     - destinationIata (wymagane),
     - startDate (wymagane),
     - endDate (wymagane),
     - minDays (wymagane),
     - maxDays (wymagane),
     - priceLimitPln (wymagane, liczba dodatnia),
     - status (aktywny/nieaktywny).
3. Edycja alertu:
   - użytkownik może edytować każdy parametr alertu (w tym limit ceny) oraz aktywować/dezaktywować alert,
   - walidacje jak przy dodawaniu.
4. Usunięcie alertu:
   - użytkownik może usunąć alert,
   - usunięcie jest nieodwracalne w MVP.
5. Walidacje biznesowe alertu:
   - endDate ≥ startDate,
   - okno dat nie większe niż 1 miesiąc: endDate - startDate ≤ 31 dni kalendarzowych,
   - minDays ≥ 1,
   - maxDays ≥ minDays,
   - maxDays - minDays ≤ 5,
   - oba loty muszą mieścić się w oknie dat:
     - dla każdej pary dat: startDate ≤ departureDate ≤ endDate oraz startDate ≤ returnDate ≤ endDate,
   - dopuszczalne są tylko daty nie wcześniejsze niż dzień uruchomienia joba (MVP: nie szukamy w przeszłości; daty w przeszłości są odrzucane).
6. Jedno origin:
   - origin jest stały w systemie (konfiguracja), nieedytowalny przez użytkownika,
   - wszystkie alerty dotyczą trasy origin -> destinationIata.

### 3.3 Generowanie par dat i zasady wyszukiwania
1. Dla każdego aktywnego alertu system generuje wszystkie pary (departureDate, returnDate) takie, że:
   - departureDate oraz returnDate należą do [startDate, endDate],
   - długość pobytu w dniach = returnDate - departureDate,
   - minDays ≤ długość pobytu ≤ maxDays.
2. W MVP nie filtrujemy po dniach tygodnia.

### 3.4 Pobieranie cen z Ryanair i deduplikacja zapytań w ciągu dnia
1. System pobiera ceny dla lotów bezpośrednich Ryanair dla danej trasy (origin, destinationIata) i konkretnej daty.
2. Redukcja zapytań w ramach jednego uruchomienia joba:
   - w ramach jednego dnia system nie odpytuje Ryanair ponownie o dane dla tego samego klucza (origin, destination, date),
   - system buforuje odpowiedź (lista dostępnych lotów i ich ceny) dla (origin, destination, date).
3. Obsługa wielu lotów tego samego dnia:
   - odpowiedź dla (origin, destination, date) może zawierać wiele lotów rozróżnionych numerem rejsu (flightNumber),
   - system rozróżnia loty w tej samej dacie po flightNumber.
4. Definicja ceny dla danej daty używana w obliczeniach:
   - cena wylotu dla departureDate to minimalna cena biletu (base fare, PLN, 1 pasażer) spośród wszystkich dostępnych lotów wylotowych w tej dacie,
   - cena powrotu dla returnDate to minimalna cena biletu (base fare, PLN, 1 pasażer) spośród wszystkich dostępnych lotów powrotnych w tej dacie.
5. Definicja ceny RT (wyświetlana jako od):
   - cenaRT = minPriceOutbound(departureDate) + minPriceInbound(returnDate),
   - jeśli dla którejkolwiek z dat nie ma dostępnego lotu (brak oferty), para dat jest odrzucana.

### 3.5 Reguła kwalifikacji do alertu i ranking wyników
1. Para dat kwalifikuje się do alertu, jeżeli cenaRT ≤ priceLimitPln.
2. System znajduje wszystkie pary dat spełniające warunek dla danego alertu.
3. W e-mailu wynikowym system prezentuje:
   - 3 najtańsze opcje (sortowanie rosnąco po cenaRT; przy remisie stabilnie po departureDate, potem returnDate),
   - liczbę wszystkich opcji spełniających warunek.

### 3.6 Harmonogram joba i kolejność wysyłki e-mail
1. Job sprawdzający uruchamia się raz dziennie o stałej porze w strefie Europe/Warsaw.
2. Wysyłka e-mail następuje po zakończeniu sprawdzania (po przetworzeniu wszystkich aktywnych alertów).

### 3.7 Powiadomienia e-mail
1. E-mail wynikowy (gdy są wyniki):
   - wysyłany codziennie bez pamięci: jeśli w danym dniu są wyniki, e-mail jest wysyłany niezależnie od tego, czy wyniki były też w dniu poprzednim,
   - agregacja antyspamowa w MVP: użytkownik otrzymuje maksymalnie 1 e-mail dziennie z wynikami, zawierający sekcje per alert, które mają co najmniej 1 opcję poniżej limitu,
   - jeśli użytkownik ma wiele alertów i kilka z nich ma wyniki, wszystkie te wyniki znajdują się w jednym e-mailu.
2. Minimalna zawartość e-maila wynikowego (dla każdej opcji w top 3):
   - cenaRT w PLN oraz oznaczenie od,
   - składowe: min cena wylotu i min cena powrotu (w PLN),
   - departureDate i returnDate,
   - origin (IATA) i destination (IATA),
   - liczba wszystkich opcji spełniających warunek dla danego alertu.
3. Dodatkowe pola informacyjne w e-mailu:
   - w MVP nie wymagamy godzin lotów; jeśli są dostępne, mogą być pokazane jako informacyjne, bez wpływu na logikę.
4. E-mail błędu (gdy nie udało się sprawdzić):
   - wysyłany maksymalnie 1 raz dziennie na użytkownika,
   - zawiera listę alertów, które nie zostały poprawnie sprawdzone, wraz z powodem (np. błąd integracji, brak odpowiedzi, błąd parsowania).
5. Brak wyników:
   - jeśli dla danego dnia alert nie ma opcji poniżej limitu i nie wystąpił błąd, system nie wysyła e-maila dla tego alertu,
   - jeśli żaden alert użytkownika nie ma wyników i nie wystąpił błąd, użytkownik nie otrzymuje e-maila tego dnia.

### 3.8 Audyt cen (zapisywanie historii)
1. Cel: umożliwienie audytu oraz analiz historycznych cen niezależnie od istnienia kont użytkowników.
2. Zakres zapisu:
   - zapisujemy tylko rekordy, gdy oferta istnieje (tj. istnieje cena biletu dla danego lotu),
   - zapis dotyczy pojedynczych lotów (a nie jedynie minimalnej ceny dnia), aby zachować spójność z rozróżnieniem po flightNumber.
3. Minimalne pola rekordu audytu (MVP):
   - checkedAt (data i czas sprawdzenia),
   - flightDate (data lotu),
   - originIata,
   - destinationIata,
   - flightNumber (jeżeli dostępny w danych źródłowych),
   - pricePln (cena biletu dla 1 pasażera, base fare).
4. Niezależność od użytkownika:
   - rekordy audytu nie są powiązane z użytkownikiem,
   - usunięcie konta usuwa dane użytkownika i jego alerty, ale rekordy audytu cen pozostają w bazie.

### 3.9 Usuwanie konta
1. Użytkownik może usunąć konto.
2. Po usunięciu konta:
   - usuwamy konto i powiązania (np. alerty),
   - nie usuwamy rekordów audytu cen.

## 4. Granice produktu
### 4.1 W zakresie MVP
1. Loty Ryanair, bezpośrednie.
2. Jedno origin skonfigurowane w systemie.
3. Destination wybierany jako konkretne lotnisko IATA.
4. Alerty z parametrami: okno dat, min/max długość pobytu, limit ceny RT w PLN.
5. Dzienne sprawdzanie o stałej porze, wysyłka e-mail po zakończeniu joba.
6. E-mail wynikowy: top 3 + liczba wszystkich opcji.
7. E-mail błędu: zbiorczy per użytkownik/dzień z powodem.
8. Audyt cen: zapis tylko gdy oferta istnieje, bez powiązania z użytkownikiem.

### 4.2 Poza MVP (explicit out of scope)
1. Loty z przesiadkami.
2. Wiele lotnisk wylotu wybieranych przez użytkownika.
3. Wybór miasta z wieloma lotniskami (np. “Londyn” z kilkoma portami) jako destination.
4. Filtry dni tygodnia.
5. Sprawdzanie cen częściej niż raz dziennie.
6. Uwzględnianie dodatków (bagaż, wybór miejsca, priority) w cenie.
7. Wielowalutowość.
8. Zaawansowana optymalizacja wydajności ponad deduplikację dzienną.

### 4.3 Założenia produktowe i konsekwencje
1. Alertowanie bez pamięci oznacza możliwość codziennych e-maili, jeśli warunek jest spełniony przez wiele dni z rzędu.
2. Zliczana cena RT jako od jest szacowaniem w oparciu o minima dzienne; nie gwarantuje, że wylot i powrót dotyczą tej samej kombinacji godzin/konkretnych rejsów (w MVP operujemy na datach, nie na połączeniu konkretnych segmentów).

## 5. Historyjki użytkowników
### 5.1 Konto, bezpieczeństwo i dostęp
- ID: US-001
  Tytuł: Rejestracja konta użytkownika
  Opis: Jako użytkownik chcę założyć konto przy użyciu e-mail i hasła, abym mógł korzystać z monitorowania cen.
  Kryteria akceptacji:
  - Po podaniu poprawnego e-mail i hasła konto zostaje utworzone.
  - System wysyła e-mail weryfikacyjny po rejestracji.
  - Użytkownik nie może aktywować alertów przed weryfikacją e-mail.

- ID: US-002
  Tytuł: Weryfikacja adresu e-mail
  Opis: Jako użytkownik chcę potwierdzić swój adres e-mail, abym mógł aktywować alerty i otrzymywać powiadomienia.
  Kryteria akceptacji:
  - Kliknięcie linku weryfikacyjnego aktywuje weryfikację e-mail dla konta.
  - Token weryfikacyjny wygasa po 24h.
  - Użyty token nie może zostać użyty ponownie.
  - Po weryfikacji użytkownik może aktywować alerty.

- ID: US-003
  Tytuł: Ponowne wysłanie linku weryfikacyjnego
  Opis: Jako użytkownik chcę móc ponownie wysłać link weryfikacyjny, gdy poprzedni wygasł lub go nie otrzymałem.
  Kryteria akceptacji:
  - Użytkownik może zainicjować resend linku w panelu lub po zalogowaniu.
  - System wysyła nowy link weryfikacyjny i unieważnia poprzedni.
  - System ogranicza liczbę wysyłek w krótkim czasie (rate limiting).

- ID: US-004
  Tytuł: Logowanie hasłem
  Opis: Jako użytkownik chcę zalogować się e-mailem i hasłem, aby zarządzać alertami.
  Kryteria akceptacji:
  - Przy poprawnych danych użytkownik zostaje zalogowany.
  - Przy błędnych danych system zwraca komunikat o błędzie bez ujawniania, czy e-mail istnieje.
  - System ogranicza liczbę nieudanych prób logowania (rate limiting).

- ID: US-005
  Tytuł: Wylogowanie
  Opis: Jako użytkownik chcę się wylogować, aby zakończyć sesję.
  Kryteria akceptacji:
  - Po wylogowaniu użytkownik nie ma dostępu do panelu bez ponownego logowania.

- ID: US-006
  Tytuł: Inicjacja resetu hasła
  Opis: Jako użytkownik chcę zresetować hasło, gdy go nie pamiętam.
  Kryteria akceptacji:
  - Użytkownik może podać e-mail i poprosić o reset hasła.
  - System wysyła e-mail z linkiem resetu (niezależnie od tego, czy konto istnieje, komunikat jest neutralny).
  - Link resetu wygasa po 1h.

- ID: US-007
  Tytuł: Ustawienie nowego hasła po resecie
  Opis: Jako użytkownik chcę ustawić nowe hasło, aby odzyskać dostęp do konta.
  Kryteria akceptacji:
  - Po użyciu ważnego tokenu użytkownik może ustawić nowe hasło.
  - Token resetu jest jednorazowy.
  - Po ustawieniu nowego hasła użytkownik może się zalogować nowym hasłem.

### 5.2 Alerty: tworzenie, edycja, usuwanie, limity i walidacje
- ID: US-008
  Tytuł: Przegląd listy alertów
  Opis: Jako użytkownik chcę widzieć listę moich alertów, aby zarządzać monitorowaniem.
  Kryteria akceptacji:
  - Lista pokazuje wszystkie alerty użytkownika.
  - Każdy alert pokazuje destinationIata, okno dat, min/max pobytu, limit ceny oraz status aktywny/nieaktywny.
  - Każdy alert pokazuje ostatni status sprawdzania: data/czas oraz informacja: znaleziono, brak wyników lub błąd.

- ID: US-009
  Tytuł: Dodanie alertu dla kierunku (IATA) z walidacjami
  Opis: Jako zweryfikowany użytkownik chcę dodać alert dla wybranego lotniska docelowego, aby system monitorował ceny.
  Kryteria akceptacji:
  - Użytkownik niezweryfikowany nie może dodać aktywnego alertu.
  - Alert wymaga: destinationIata, startDate, endDate, minDays, maxDays, priceLimitPln.
  - System odrzuca okno dat większe niż 31 dni kalendarzowych.
  - System odrzuca konfigurację, gdy maxDays - minDays > 5.
  - System odrzuca konfigurację, gdy endDate < startDate.
  - System odrzuca daty w przeszłości.

- ID: US-010
  Tytuł: Ograniczenie liczby alertów
  Opis: Jako użytkownik chcę mieć limit liczby alertów, aby system był prosty w MVP.
  Kryteria akceptacji:
  - Użytkownik nie może mieć więcej niż 5 aktywnych alertów.
  - Próba dodania lub aktywacji 6. alertu kończy się czytelnym błędem walidacji.

- ID: US-011
  Tytuł: Edycja alertu
  Opis: Jako użytkownik chcę edytować parametry alertu, aby dostosować monitorowanie do moich potrzeb.
  Kryteria akceptacji:
  - Użytkownik może zmienić destinationIata, okno dat, min/max pobytu i limit ceny.
  - Obowiązują te same walidacje co przy dodawaniu.
  - Zmiany są zapisane i widoczne na liście alertów.

- ID: US-012
  Tytuł: Aktywacja i dezaktywacja alertu
  Opis: Jako użytkownik chcę móc włączać i wyłączać alert, aby kontrolować, czy ma być sprawdzany w jobie.
  Kryteria akceptacji:
  - Użytkownik może przełączyć status alertu na aktywny/nieaktywny.
  - Użytkownik bez weryfikacji e-mail nie może aktywować alertu.
  - Job uwzględnia wyłącznie alerty aktywne.

- ID: US-013
  Tytuł: Usunięcie alertu
  Opis: Jako użytkownik chcę usunąć alert, którego już nie potrzebuję.
  Kryteria akceptacji:
  - Użytkownik może usunąć alert.
  - Po usunięciu alert nie pojawia się na liście.

### 5.3 Powiadomienia e-mail i zachowanie dzienne
- ID: US-014
  Tytuł: Otrzymanie dziennego e-maila z wynikami dla alertów
  Opis: Jako użytkownik chcę otrzymać e-mail, gdy system znajdzie opcje RT poniżej mojego limitu.
  Kryteria akceptacji:
  - E-mail jest wysyłany tylko dla użytkownika ze zweryfikowanym e-mail.
  - E-mail zawiera sekcje dla wszystkich alertów, które mają co najmniej 1 opcję poniżej limitu danego dnia.
  - Dla każdego alertu e-mail zawiera top 3 najtańsze opcje oraz liczbę wszystkich opcji.
  - Dla każdej opcji podana jest cenaRT (od) i składowe (wylot i powrót), daty i IATA origin/destination.

- ID: US-015
  Tytuł: Codzienny charakter alertowania bez pamięci
  Opis: Jako użytkownik chcę, aby system informował mnie codziennie, jeśli warunek nadal jest spełniony, abym mógł podjąć decyzję w dowolnym dniu.
  Kryteria akceptacji:
  - Jeżeli warunek jest spełniony przez kilka dni z rzędu, e-mail może przychodzić codziennie.
  - System nie tłumi powiadomień na podstawie historii poprzednich dni.

- ID: US-016
  Tytuł: Brak e-maila przy braku wyników
  Opis: Jako użytkownik nie chcę dostawać e-maili, jeśli nie ma ofert poniżej limitu, aby uniknąć spamu.
  Kryteria akceptacji:
  - Jeśli danego dnia nie ma żadnej opcji poniżej limitu dla żadnego alertu, użytkownik nie otrzymuje e-maila wynikowego.

- ID: US-017
  Tytuł: Otrzymanie zbiorczego e-maila o błędach sprawdzania
  Opis: Jako użytkownik chcę otrzymać informację, gdy system nie był w stanie sprawdzić moich alertów, aby wiedzieć, że brak e-maila nie zawsze oznacza brak okazji.
  Kryteria akceptacji:
  - Jeśli co najmniej jeden alert nie został poprawnie sprawdzony danego dnia, użytkownik otrzymuje maksymalnie 1 e-mail błędu tego dnia.
  - E-mail zawiera listę alertów, których nie udało się sprawdzić, oraz powód.

### 5.4 Usuwanie konta i dane
- ID: US-018
  Tytuł: Usunięcie konta użytkownika
  Opis: Jako użytkownik chcę usunąć konto, aby przestać korzystać z aplikacji i usunąć swoje dane.
  Kryteria akceptacji:
  - Użytkownik może usunąć konto z poziomu aplikacji.
  - Po usunięciu konta użytkownik nie może się zalogować.
  - Alerty użytkownika są usuwane.
  - Rekordy audytu cen pozostają w bazie i nie są powiązane z kontem.

### 5.5 Scenariusze skrajne i alternatywne (walidacje, błędy, limity)
- ID: US-019
  Tytuł: Obsługa niepoprawnego okna dat
  Opis: Jako użytkownik chcę jasny komunikat, gdy podam błędne daty, abym mógł szybko poprawić konfigurację.
  Kryteria akceptacji:
  - System nie pozwala zapisać alertu z endDate < startDate.
  - System pokazuje czytelny komunikat walidacyjny.

- ID: US-020
  Tytuł: Obsługa zbyt szerokiego okna dat
  Opis: Jako użytkownik chcę wiedzieć, że okno dat jest ograniczone w MVP, abym nie tworzył alertów zbyt złożonych.
  Kryteria akceptacji:
  - System nie pozwala zapisać alertu z oknem dat > 31 dni kalendarzowych.
  - System pokazuje czytelny komunikat walidacyjny.

- ID: US-021
  Tytuł: Obsługa niepoprawnego zakresu długości pobytu
  Opis: Jako użytkownik chcę jasny komunikat, gdy podam błędny zakres pobytu.
  Kryteria akceptacji:
  - System nie pozwala zapisać alertu z maxDays < minDays.
  - System nie pozwala zapisać alertu z maxDays - minDays > 5.
  - System pokazuje czytelny komunikat walidacyjny.

- ID: US-022
  Tytuł: Obsługa braku ofert dla części dat
  Opis: Jako użytkownik chcę, aby system poprawnie ignorował pary dat, gdzie brakuje lotu wylotowego lub powrotnego.
  Kryteria akceptacji:
  - Para dat jest liczona tylko wtedy, gdy dla obu dat istnieje oferta.
  - System nie wysyła błędu, jeśli brak oferty jest normalnym stanem (to nie jest błąd integracji).

### 5.6 Historyjki operacyjne (operator systemu)
- ID: US-023
  Tytuł: Konfiguracja origin w systemie
  Opis: Jako operator systemu chcę skonfigurować jedno lotnisko wylotu, aby użytkownicy wybierali trasy tylko z tego miejsca.
  Kryteria akceptacji:
  - System posiada konfigurację originIata.
  - Wszystkie alerty są interpretowane względem originIata.

- ID: US-024
  Tytuł: Uruchomienie i monitoring dziennego joba
  Opis: Jako operator systemu chcę, aby job uruchamiał się raz dziennie i był obserwowalny, aby wykrywać awarie integracji.
  Kryteria akceptacji:
  - Job uruchamia się raz dziennie o stałej porze w Europe/Warsaw.
  - System loguje start, koniec i status joba oraz liczbę przetworzonych alertów.
  - Błędy integracji są rejestrowane i wykorzystywane do generowania e-maili błędów.

## 6. Metryki sukcesu
### 6.1 Skuteczność wykrywania okazji
1. Odsetek dni, w których dla aktywnych alertów system znajduje co najmniej 1 parę RT poniżej limitu (w podziale na alert).
2. Liczba par RT spełniających warunek na alert na dzień (średnia, mediana).

### 6.2 Skuteczność powiadomień
1. Odsetek dni z poprawnie wysłanymi e-mailami wynikowymi (sukces wysyłki) względem dni, w których były wyniki.
2. Odsetek dni z e-mailami błędu na użytkownika (częstotliwość problemów integracji).

### 6.3 Użyteczność wyników dla użytkownika
1. Odsetek alertów, które w ciągu zadanego okresu wygenerowały co najmniej 1 wynik.
2. Średnia liczba dni do pierwszego trafienia (czas od utworzenia alertu do pierwszego e-maila wynikowego).


### 6.5 Checklist po PRD
1. Czy każdą historię użytkownika można przetestować? Tak, wszystkie US-001..US-024 posiadają jednoznaczne kryteria akceptacji.
2. Czy kryteria akceptacji są jasne i konkretne? Tak, opisują warunki wejścia/wyjścia oraz limity MVP.
3. Czy mamy wystarczająco dużo historyjek użytkownika, aby zbudować w pełni funkcjonalną aplikację? Tak, obejmują rejestrację, weryfikację e-mail, logowanie, reset hasła, pełny cykl życia alertów, dzienne sprawdzanie i wysyłkę e-mail oraz usuwanie konta.
4. Czy uwzględniliśmy wymagania dotyczące uwierzytelniania i autoryzacji? Tak, obejmują logowanie, reset hasła, weryfikację e-mail oraz podstawowe wymagania bezpieczeństwa dostępu.
