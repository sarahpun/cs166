DROP INDEX IF EXISTS part_nyc_number_index;
DROP INDEX IF EXISTS part_nyc_supplier_index;
DROP INDEX IF EXISTS part_nyc_color_index;
DROP INDEX IF EXISTS part_nyc_number_index;
DROP INDEX IF EXISTS part_sfo_on_hand_index;


CREATE INDEX part_nyc_number_index
ON part_nyc 
USING BTREE
(part_number);

CREATE INDEX part_nyc_supplier_index
ON part_nyc 
USING BTREE
(supplier);

CREATE INDEX part_nyc_color_index
ON part_nyc
USING BTREE
(color);

CREATE INDEX part_nyc_on_hand_index
ON part_nyc
USING BTREE
(on_hand);

CREATE INDEX part_sfo_number_index
ON part_sfo 
USING BTREE
(part_number);
