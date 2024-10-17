-- Find the total number of parts supplied by each supplier.
SELECT S.sname, COUNT(*)
FROM parts P, suppliers S, catalog C
WHERE P.pid = C.pid AND S.sid = C.sid
GROUP BY S.sid;
-- Find the total number of parts supplied by each supplier who supplies at least 3 parts.
SELECT S.sname, COUNT(*)
FROM parts P, suppliers S, catalog C
WHERE P.pid = C.pid AND S.sid = C.sid
GROUP BY S.sid HAVING COUNT(C.pid) >= 3;

-- For every supplier that supplies only green parts, print the name of the supplier and the
-- total number of parts that he supplies.
SELECT S.sid, S.sname, COUNT(*) 
FROM suppliers S, parts P, catalog C 
WHERE S.sid = C.sid AND P.pid = C.pid AND S.sid IN (
    SELECT S.sid 
    FROM suppliers S, parts P, catalog C 
    WHERE S.sid = C.sid AND P.pid = C.pid AND P.color = 'Green' 
EXCEPT
    SELECT C.sid 
    FROM suppliers S, parts P, catalog C
    WHERE C.pid = P.pid AND p.color != 'Green') AND S.sid = C.sid AND P.pid = C.pid 
GROUP BY S.sid;

-- For every supplier that supplies green part and red part, print the name of the supplier and
-- the price of the most expensive part that he supplies.
SELECT S.sname, MAX(C.cost)
FROM Suppliers S, Parts P, Catalog C
WHERE S.sid = C.sid AND P.pid = C.pid AND S.sid IN (
    SELECT S.sid
    FROM Suppliers S, Parts P, Catalog C
    WHERE S.sid = C.sid AND P.pid = C.pid AND P.color = 'Green'
    INTERSECT
    Select S.sid
    FROM Suppliers S, Parts P, Catalog C
    WHERE S.sid = C.sid AND P.pid = C.pid AND P.color = 'Red')
GROUP BY S.sid;