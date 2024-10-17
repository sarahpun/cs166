DROP SEQUENCE IF EXISTS part_number_seq;
CREATE SEQUENCE part_number_seq START WITH 50000;

DROP FUNCTION IF EXISTS get_next_part_number();

CREATE OR REPLACE FUNCTION get_next_part_number()
RETURNS trigger AS
$body$
BEGIN  
    NEW.part_number := nextval('part_number_seq');
    RETURN NEW;
END 
$body$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS trigger_part_nyc_part_number ON part_nyc;

CREATE TRIGGER trigger_part_nyc_part_number
BEFORE INSERT ON part_nyc
FOR EACH ROW
EXECUTE PROCEDURE get_next_part_number();