-- drop sequences if they exist
DROP SEQUENCE IF EXISTS rentalorder_seq;
DROP SEQUENCE IF EXISTS trackinginfo_seq;

-- drop functions if they exist
DROP FUNCTION IF EXISTS generate_rentalorder_id();
DROP FUNCTION IF EXISTS generate_tracking_id();

-- drop the trigger if it exists 
DROP TRIGGER IF EXISTS rentalorder_id_trigger ON RentalOrder;
DROP TRIGGER IF EXISTS tracking_id_trigger ON TrackingInfo;


-- creat new sequences starting from 4147 to continue from where csv file left off
CREATE SEQUENCE rentalorder_seq START 4147;
CREATE SEQUENCE trackinginfo_seq START 4147;

-- create trigger function for generating rental order IDs
CREATE OR REPLACE FUNCTION generate_rentalorder_id() RETURNS TRIGGER AS $$
BEGIN
  NEW.rentalOrderID := 'gamerentalorder' || nextval('rentalorder_seq');
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- create trigger function for generating tracking IDs
CREATE OR REPLACE FUNCTION generate_tracking_id() RETURNS TRIGGER AS $$
BEGIN
  NEW.trackingID := 'trackingid' || nextval('trackinginfo_seq');
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- create triggers for rental order ID generation
CREATE TRIGGER rentalorder_id_trigger
BEFORE INSERT ON RentalOrder
FOR EACH ROW
EXECUTE PROCEDURE generate_rentalorder_id();

-- create triggers for tracking ID generation
CREATE TRIGGER tracking_id_trigger
BEFORE INSERT ON TrackingInfo
FOR EACH ROW
EXECUTE PROCEDURE generate_tracking_id();
