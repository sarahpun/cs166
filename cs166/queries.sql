
-- Count how many parts in NYC have more than 70 parts on_hand
SELECT COUNT(*)
FROM part_nyc P
WHERE on_hand > 70;

-- Count how many total parts on_hand, in both NYC and SFO, are Red
SELECT SUM(total) AS total_nyc_sfo
FROM(
    SELECT SUM(on_hand) AS total
    FROM part_nyc
    WHERE color IN(
        SELECT color_id
        FROM color
        WHERE color_name = 'Red'
    )
    UNION
    SELECT SUM(on_hand) AS total
    FROM part_sfo
    WHERE color IN(
        SELECT color_id
        FROM color
        WHERE color_name = 'Red'
    )
) AS subquery;

-- List all the suppliers that have more total on_hand parts in NYC than they do in SFO.
SELECT S.supplier_id, S.supplier_name
FROM supplier S
JOIN (
    SELECT supplier, SUM(on_hand) AS total_nyc_parts
    FROM part_nyc
    GROUP BY supplier
) AS nyc ON S.supplier_id = nyc.supplier
JOIN (
    SELECT supplier, SUM(on_hand) AS total_sfo_parts
    FROM part_sfo
    GROUP BY supplier
) AS sfo ON S.supplier_id = sfo.supplier
WHERE total_nyc_parts > total_sfo_parts;

-- List all suppliers that supply parts in NYC that aren’t supplied by anyone in SFO.
SELECT DISTINCT S.supplier_id, S.supplier_name
FROM supplier S
JOIN part_nyc N ON S.supplier_id = N.supplier
WHERE NOT EXISTS (
    SELECT *
    FROM part_sfo sf 
    WHERE sf.part_number = N.part_number
);

-- Update all of the NYC on_hand values to on_hand - 10.
UPDATE part_nyc
SET on_hand = on_hand - 10;

-- Delete all parts from NYC which have less than 30 parts on_hand
DELETE FROM part_nyc
WHERE on_hand < 30;
