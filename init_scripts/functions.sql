-- Рассчитать сумму значений поля height для всех объектов.
CREATE
OR REPLACE FUNCTION get_height_sum() RETURNS BIGINT AS $$
BEGIN
RETURN (SELECT SUM(height)
        FROM person);
END;
$$
LANGUAGE plpgsql;

--  Вернуть массив объектов, значение поля name которых начинается с заданной подстроки.
CREATE
OR REPLACE FUNCTION get_start_with(str TEXT) RETURNS SETOF person AS $$
BEGIN
RETURN QUERY
SELECT *
FROM person
WHERE name LIKE str || '%';
END;
$$
LANGUAGE plpgsql;

-- Вернуть массив уникальных значений поля height по всем объектам.
CREATE
OR REPLACE FUNCTION get_unique_heights() RETURNS REAL[] AS $$
BEGIN
RETURN (SELECT ARRAY(
                   SELECT DISTINCT height
                       FROM person
               ));
END;
$$
LANGUAGE plpgsql;


-- Вывести долю людей с заданным цветом волос в общей популяции (в процентах)
CREATE
OR REPLACE FUNCTION get_hair_color_percentage(hair_color_arg TEXT) RETURNS NUMERIC AS $$
DECLARE
total_population INTEGER;
    color_population
INTEGER;
    percentage
NUMERIC;
BEGIN
-- Общее количество людей
SELECT COUNT(*)
INTO total_population
FROM person;

-- Количество людей с заданным цветом волос
SELECT COUNT(*)
INTO color_population
FROM person
WHERE hair_color = hair_color_arg;

-- Проверка деления на ноль (если нет людей в таблице)
IF
total_population = 0 THEN
        RETURN 0;
END IF;
    -- Вычисление процента
    percentage
:= (color_population::NUMERIC / total_population) * 100;

RETURN percentage;
END;
$$
LANGUAGE plpgsql;


CREATE
OR REPLACE FUNCTION get_people_count_by_hair_color_in_location(
    hair_color_param TEXT,
    x_arg BIGINT,
    y_arg BIGINT,
    z_arg Float
) RETURNS INTEGER AS $$
DECLARE
count_people INTEGER;
BEGIN

SELECT COUNT(*)
INTO count_people
FROM person
WHERE hair_color = hair_color_param
  AND location_id in (select id
                      from location
                      where x = x_arg
                        and y = y_arg
                        and z = z_arg);

RETURN count_people;

END;
$$
LANGUAGE plpgsql;