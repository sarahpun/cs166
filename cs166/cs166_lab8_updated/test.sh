#!/bin/bash

echo "Dropping/creating tables..."
cp -a *.dat $PGDATA/
cs166_psql $USER'_lab8_DB' < create_tables.sql


echo "Creating trigger and procedure..."
cs166_psql $USER"_lab8_DB" < triggers.sql

echo "Testing insert..."
cat <(echo "Insert into part_nyc(supplier, color, on_hand, descr) Values (0, 0, 20, 'Description')")|cs166_psql $USER"_lab8_DB"
cat <(echo "Insert into part_nyc(supplier, color, on_hand, descr) Values (1, 1, 20, 'Description')")|cs166_psql $USER"_lab8_DB"
cat <(echo "Insert into part_nyc(supplier, color, on_hand, descr) Values (2, 0, 20, 'Description')")|cs166_psql $USER"_lab8_DB"
cat <(echo 'SELECT part_number FROM part_nyc WHERE on_hand=20;')|cs166_psql $USER"_lab8_DB"