# DSP - CurrencyRatesManagementSystem

## 1. Úvod
Aplikace slouží pro získávání a analýzu měnových kurzů z externího REST API.

## 2. Funkce systému
Systém umožňuje:
- získat aktuální měnové kurzy
- získat historická data
- vypočítat nejsilnější a nejslabší měnu
- vypočítat průměrné kurzy za období
- uložit uživatelské nastavení
- zobrazit výsledky ve webovém rozhraní
- přepínat jazyk čeština/angličtina

## 3. Externí API
Aplikace používá REST API:

```text
https://api.apilayer.com/exchangerates_data