Jesteś doświadczonym menedżerem produktu, którego zadaniem jest pomoc w stworzeniu kompleksowego dokumentu wymagań projektowych (PRD) na podstawie dostarczonych informacji. Twoim celem jest wygenerowanie listy pytań i zaleceń, które zostaną wykorzystane w kolejnym promptowaniu do utworzenia pełnego PRD.

Prosimy o uważne zapoznanie się z poniższymi informacjami:

<project_description>
# Aplikacja - AirlineTickets (MVP)

## Główny problem
W serwisie Ryanair często zmieniją się ceny biletów. Jeśli użytkownik chce znaleźć tani lot musi codziennie logować się na stronę https://www.ryanair.com/
i sprawdzać ceny lotów na różnych kierunkach. Aplikacja codziennie pobiera aktualne ceny biletów. Użytkownik może się zalogować, założyć konto i ustalić limit ceny biletu na określonym
kierunku. Jeśli danego dnia cena biletu spadnie poniżej tej kwoty, użytkownik dostaje powiadomienie w postaci maila.

## Najmniejszy zestaw funkcjonalności
- Prosty system kont użytkowników. Użytkownik może zdefiniować kierunek lotu, długość pobytu, max cenę jaką może zapłacić za lot (tam i z powrotem), ilość osób i wiek osób (jeśli leci z rodziną)
przedział w którym będzie wyszukiwanie (np. wszyukaj loty na Maltę, tydzień pobytu, w lipcu 2026). 
- Lista lotnisk z których uzytkownik może lecieć znajduje sie w pliku koniguracyjnym. W wersji MVP będzie tylko jedno lotnisko: Wrocław
- Aplikacja codzienie sprawdza ceny biletów wg kryteriów zadanych przez wszystkich użytkowników w systemie.
- Jeśli Aplikacja znajdzie odpowiedni lot, wysyła emaila do użytkownika.
- Aplikacja cachuje odpowiedzi. Jeśli użytkownikB chce sprawdzić ten sam kierunek/datę/il. paseżerów co użytkownikA, aplikacją NIE odpytuje ponownie ryainair API.
- Wszystkie sprawdzane ceny biletów są zapisywane są w bazie danych (w celu póżniejszego audytu).



## Co NIE wchodzi w zakres MVP
- nie sprawdzamy przesiadek. Użytkownik może lecieć tylko bezpośrednio.
- nie przejmujemy się wydajnością. Na początku będzie mało użytkowników, i tylko dla lotniska Wrocław.
- sprawdzamy ceny bilietów raz dziennie o tej samej porze (chociaż ceny biletów zmieniają się także w środku dnia) 

## Kryteria sukcesu
Aplikacja jest w stanie zareagować na niską cenę biletu i na czas powiadomić użytkownika.
</project_description>

Przeanalizuj dostarczone informacje, koncentrując się na aspektach istotnych dla tworzenia PRD. Rozważ następujące kwestie:
<prd_analysis>
1. Zidentyfikuj główny problem, który produkt ma rozwiązać.
2. Określ kluczowe funkcjonalności MVP.
3. Rozważ potencjalne historie użytkownika i ścieżki korzystania z produktu.
4. Pomyśl o kryteriach sukcesu i sposobach ich mierzenia.
5. Oceń ograniczenia projektowe i ich wpływ na rozwój produktu.
</prd_analysis>

Na podstawie analizy wygeneruj listę 10 pytań i zaleceń w formie łączonej (pytanie + zalecenie). Powinny one dotyczyć wszelkich niejasności, potencjalnych problemów lub obszarów, w których potrzeba więcej informacji, aby stworzyć skuteczny PRD. Rozważ pytania dotyczące:

1. Szczegółów problemu użytkownika
2. Priorytetyzacji funkcjonalności
3. Oczekiwanego doświadczenia użytkownika
4. Mierzalnych wskaźników sukcesu
5. Potencjalnych ryzyk i wyzwań
6. Harmonogramu i zasobów

<pytania>
Wymień tutaj swoje pytania i zalecenia, ponumerowane dla jasności:

Przykładowo:
1. Czy już od startu projektu planujesz wprowadzenie płatnych subskrypcji?

Rekomendacja: Pierwszy etap projektu może skupić się na funkcjonalnościach darmowych, aby przyciągnąć użytkowników, a płatne funkcje można wprowadzić w późniejszym etapie.
</pytania>

Kontynuuj ten proces, generując nowe pytania i rekomendacje w oparciu o odpowiedzi użytkownika, dopóki użytkownik wyraźnie nie poprosi o podsumowanie.

Pamiętaj, aby skupić się na jasności, trafności i dokładności wyników. Nie dołączaj żadnych dodatkowych komentarzy ani wyjaśnień poza określonym formatem wyjściowym.

Pracę analityczną należy przeprowadzić w bloku myślenia. Końcowe dane wyjściowe powinny składać się wyłącznie z pytań i zaleceń i nie powinny powielać ani powtarzać żadnej pracy wykonanej w sekcji prd_analysis.