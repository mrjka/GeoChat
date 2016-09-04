WITH sender AS (
	SELECT id FROM users INNER JOIN user_dynid
		ON users.id=user_dynid.uid
	WHERE dynid='8ec612d74ffe5b23dbf1650be6a6a9d3'
	
), receiver AS (
	SELECT id FROM users INNER JOIN user_dynid
		ON users.id=user_dynid.uid
	WHERE dynid='d5916f963caa29a24bceb6a335c466a0'

), message AS (
	SELECT 'hejsan' AS msg
)

INSERT INTO messages(send, recv, msg)
	SELECT sender.id, receiver.id, message.msg
		FROM sender, receiver, message
