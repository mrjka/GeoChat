WITH online_users AS (
	SELECT name, geog, user_dynid.dynid AS dynid, age, email, description
	FROM users INNER JOIN user_dynid ON id=uid INNER JOIN user_loc ON
	user_dynid.dynid = user_loc.dynid
	
), main_user AS (
	SELECT geog FROM online_users WHERE dynid = '8ec12d74ffe5b23dbf1650be6a6a9d3'
	
), other_users AS (
	SELECT name, age, email, description, geog FROM online_users
	WHERE dynid != '8ec612d74ffe5b23dbf1650be6a6a9d3'
)

SELECT name, age, email, description 
FROM main_user CROSS JOIN other_users
WHERE ST_DWithin(main_user.geog, other_users.geog, 5000)

