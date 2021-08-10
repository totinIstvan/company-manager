# A Company Manager applikáció végpontjai:

 ## 1. company-controller:

`/api/companies`	- GET/ A Doe Corporation összes leányvállalatának listázása

Lehetőségek:

- Paraméter ?full=true – a vállalatok listázása osztályokkal együtt
- Paraméter ?full=false (vagy nincs) – a vállalatok listázása osztályok nélkül

`/api/companies/{id}`	- GET/ A Doe Corporation meghatározott leányvállalatának kiíratása id alapján

Lehetőségek:

- Paraméter ?full=true – a vállalat kiíratása osztályokkal együtt
- Paraméter ?full=false (vagy nincs) – a vállalat kiíratása osztályok nélkül

`/api/companies`	- POST/ Új leányvállalat felvétele az adatbázisba (osztály-listával, vagy nélküle)

`/api/companies/{id}`	- PUT/ Meglévő leányvállalat adatainak módosítása id alapján

`/api/companies/{id}`	- DELETE/ Leányvállalat eltávolítása az adatbázisból (csak abban az esetben lehetséges, ha nincs hozzá tartozó osztály és/vagy alkalmazott, ellenkező esetben hibaüzenetben figyelmeztet)

`/api/companies/{id}/employees`	- GET/ Meghatározott leányvállalat alkalmazotti listájának kiíratása id alapján

`/api/companies/{companyId}/departments/{departmentId}/employees` - GET/ Meghatározott leányvállalat meghatározott osztálya alkalmazotti listájának kiíratása id-k alapján


## 2. department-controller:

`/api/departments`	- GET/ Az összes osztály listázása

`/api/departments/{id}`	- GET/ Meghatározott osztály kiíratása id alapján

`/api/departments`	- POST/ Új osztály felvétele

`/api/departments/{id}`	- PUT/ Létező osztály nevének megváltoztatása id alapján

`/api/departments/{id}`	- DELETE/ Osztály eltávolítása az adatbázisból id alapján (csak abban az esetben lehetséges, ha nincs hozzárendelve vállalathoz, ellenkező esetben hibaüzenetben figyelmeztet)



## 3. employee-controller:

`/api/employees`	- GET/ Az összes leányvállalat összes alkalmazottjának listázása

Lehetőségek:

- Paraméter ?full=true – az alkalmazottak listázása a hozzájuk tartozó osztállyal és vállalattal
-  Paraméter ?full=false (vagy nincs) – az alkalmazottak listázása osztályok és vállalatok nélkül

`/api/employees/{id}`	- GET/ Meghatározott alkalmazott adatainak kiíratása id alapján

Lehetőségek:

- Paraméter ?full=true – az alkalmazott adatainak kiíratása a 	hozzá tartozó osztállyal és vállalattal
- Paraméter ?full=false (vagy nincs) – az alkalmazott adatainak 	kiíratása osztály és vállalat nélkül

`/api/employees`	- POST/ Új alkalmazott felvétele az adatbázisba (kizárólag vállalat 	és osztály megjelölésével)

`/api/employees/{id}`	- PUT/ Meglévő alkalmazott adatainak módosítása id alapján

`/api/employees/{id}`	- DELETE/ Alkalmazott eltávolítása az adatbázisból id alapján

`/api/employees/limit`	- GET/ Paraméter ?limit=12000 – A megadott limitet meghaladó fizetéssel rendelkező alkalmazottak listája

### ***hivatkozások:***

http://localhost:8080/v3/api-docs

http://localhost:8080/swagger-ui/