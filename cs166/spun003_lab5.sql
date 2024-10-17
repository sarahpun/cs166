--find the pid of parts with cost lower than 10
SELECT P.pid
FROM parts P, catalog C
WHERE C.pid = P.pid AND C.cost < 10;

--find the name of parts with cost lower than 10
SELECT P.pname
FROM parts P, catalog C
WHERE C.pid = P.pid AND C.cost < 10;

--find the address of the suppliers who supply "Fire Hydrant Cap"
SELECT S.address
FROM suppliers S, parts P, catalog C
WHERE S.sid = C.sid AND C.pid = P.pid AND P.pname = 'Fire Hydrant Cap';

--Find the name of the suppliers who supply green parts
SELECT S.sname
FROM suppliers S, parts P, catalog C
WHERE S.sid = C.sid AND C.pid = P.pid AND P.color = 'green';

--for each supplier, list the supplier's name along with all part's name that it supply
SELECT S.sname, P.pname
FROM suppliers S, parts P, catalog C
WHERE S.sid = C.sid AND C.pid = P.pid;