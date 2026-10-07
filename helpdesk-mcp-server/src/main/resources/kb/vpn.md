# Jak skonfigurować VPN

## Cel

VPN umożliwia bezpieczny dostęp do firmowych zasobów podczas pracy
poza biurem, np. do aplikacji wewnętrznych i serwerów plików.

## Wymagania

Przed rozpoczęciem upewnij się, że masz:

- aktywne konto firmowe z uprawnieniami do VPN;
- działające połączenie z Internetem;
- komputer zgodny z polityką bezpieczeństwa organizacji;
- skonfigurowane uwierzytelnianie wieloskładnikowe (MFA);
- zatwierdzonego klienta VPN: [NAZWA KLIENTA VPN].

Dane organizacyjne:

- adres bramy VPN: [ADRES BRAMY VPN];
- źródło instalatora lub profilu: [FIRMOWY PORTAL OPROGRAMOWANIA];
- kontakt do wsparcia: [KANAŁ KONTAKTU Z HELPDESKIEM].

Nie zgaduj adresu bramy ani nie pobieraj konfiguracji z nieznanych źródeł.
Jeżeli tych danych brakuje, skontaktuj się z helpdeskiem.

## Konfiguracja krok po kroku

### 1. Sprawdź połączenie z Internetem

Otwórz publiczną stronę internetową.

Jeśli korzystasz z hotelowej lub publicznej sieci Wi-Fi, może być konieczne
zalogowanie się do portalu tej sieci przed uruchomieniem VPN.
Nie podawaj w nim firmowego hasła.

### 2. Zainstaluj klienta VPN

Zainstaluj zatwierdzoną aplikację z firmowego portalu oprogramowania.

Jeśli aplikacja wymaga uprawnień administratora, których nie posiadasz,
poproś helpdesk o instalację. Nie obchodź zabezpieczeń urządzenia.

### 3. Dodaj profil połączenia

Jeżeli profil jest zarządzany automatycznie, wybierz istniejący profil firmowy.

W przeciwnym razie:

1. Otwórz klienta VPN.
2. Dodaj połączenie zgodnie z instrukcją dla używanego klienta.
3. Wpisz adres bramy przekazany przez dział IT.
4. Zastosuj ustawienia lub zaimportuj profil dostarczony przez dział IT.

Nie zmieniaj samodzielnie ustawień certyfikatów, DNS ani tras sieciowych.

### 4. Zaloguj się

1. Wybierz firmowy profil i rozpocznij połączenie.
2. Zaloguj się przez zatwierdzony firmowy mechanizm logowania.
3. Potwierdź żądanie MFA, które wynika z właśnie rozpoczętego logowania.
4. Poczekaj na status „Połączono” lub jego odpowiednik.

Nie zatwierdzaj niespodziewanych żądań MFA.

### 5. Sprawdź dostęp

Otwórz firmowy zasób, do którego masz uprawnienia.

Jeżeli VPN jest połączony, ale jeden zasób pozostaje niedostępny,
problem może dotyczyć uprawnień lub samej aplikacji, a nie połączenia VPN.

### 6. Rozłącz połączenie

Po zakończeniu pracy rozłącz VPN, jeżeli polityka organizacji na to pozwala.
Nie wyłączaj mechanizmu Always-On VPN na zarządzanym urządzeniu.

## Rozwiązywanie problemów

### Brak połączenia lub timeout

1. Sprawdź dostęp do Internetu.
2. Sprawdź poprawność wybranego profilu i adresu bramy.
3. Uruchom ponownie klienta VPN i ponów próbę.
4. Sprawdź komunikaty o awarii usług firmowych.
5. Jeśli polityka organizacji pozwala, sprawdź połączenie przez inną
   zaufaną sieć.

Nie wyłączaj zapory ani ochrony antywirusowej.

### Błąd logowania

- Sprawdź nazwę użytkownika i komunikat błędu.
- Jeśli hasło wygasło, użyj oficjalnego procesu jego zmiany.
- Jeżeli konto jest zablokowane, skontaktuj się z helpdeskiem.
- Nie ponawiaj wielokrotnie logowania tymi samymi błędnymi danymi.

### Brak powiadomienia MFA

- Sprawdź połączenie telefonu z Internetem.
- Otwórz zatwierdzoną aplikację uwierzytelniającą.
- Upewnij się, że data i godzina urządzenia są ustawiane automatycznie.
- Jeśli problem trwa, skorzystaj z oficjalnej procedury odzyskiwania dostępu.

Nigdy nie przekazuj kodów MFA ani kodów odzyskiwania w zgłoszeniu.

### Błąd certyfikatu

Przerwij próbę połączenia i zgłoś problem do helpdesku.
Nie ignoruj ostrzeżenia i nie wyłączaj weryfikacji certyfikatu.

### VPN jest połączony, ale aplikacja nie działa

- Sprawdź, czy problem dotyczy jednej aplikacji, czy wszystkich zasobów.
- Zapisz dokładny komunikat błędu.
- Ustal, czy inni użytkownicy mają ten sam problem.
- Nie zmieniaj samodzielnie DNS ani routingu.

## Co podać w zgłoszeniu

- system operacyjny i jego wersję;
- nazwę i wersję klienta VPN;
- czas wystąpienia problemu wraz ze strefą czasową;
- dokładny komunikat błędu;
- informację, czy wcześniej połączenie działało;
- zakres problemu: jedna osoba, zespół czy wiele zespołów;
- wpływ na pracę i dostępne obejście;
- wykonane już kroki diagnostyczne.

Nie dołączaj haseł, tokenów, kodów MFA, kluczy prywatnych ani pełnych
plików konfiguracyjnych zawierających sekrety. Zrzuty ekranu i logi
przejrzyj oraz zanonimizuj przed wysłaniem.