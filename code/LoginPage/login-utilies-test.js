const assert = require('node:assert/strict');
const test = require('node:test');
const {
	authenticateAccount,
	getCredentials,
	initializeDatabase,
	registerAccount
} = require('./login-utilies');

test('getCredentials trims usernames and requires non-empty string credentials', () => {
	assert.deepEqual(getCredentials({ username: '  ada  ', password: 'secret' }), {
		username: 'ada',
		password: 'secret'
	});
	assert.equal(getCredentials({ username: ' ', password: 'secret' }), null);
	assert.equal(getCredentials({ username: 'ada', password: '' }), null);
	assert.equal(getCredentials({ username: ['ada'], password: 'secret' }), null);
});

test('authenticateAccount returns missing-credentials without querying', async () => {
	let queried = false;
	const result = await authenticateAccount({ username: '', password: 'secret' }, async () => {
		queried = true;
	});

	assert.deepEqual(result, { status: 'missing-credentials' });
	assert.equal(queried, false);
});

test('authenticateAccount queries with normalized credentials and returns the account', async () => {
	let query;
	const result = await authenticateAccount(
		{ username: '  ada ', password: 'secret' },
		async (sql, params) => {
			query = { sql, params };
			return { username: 'ada' };
		}
	);

	assert.equal(query.sql, 'SELECT username FROM accounts WHERE username = ? AND password = ?');
	assert.deepEqual(query.params, ['ada', 'secret']);
	assert.deepEqual(result, { status: 'authenticated', username: 'ada' });
});

test('authenticateAccount returns invalid-credentials when no account matches', async () => {
	const result = await authenticateAccount(
		{ username: 'ada', password: 'incorrect' },
		async () => undefined
	);

	assert.deepEqual(result, { status: 'invalid-credentials' });
});

test('authenticateAccount propagates database errors', async () => {
	const failure = new Error('Database unavailable');

	await assert.rejects(
		authenticateAccount({ username: 'ada', password: 'secret' }, async () => {
			throw failure;
		}),
		failure
	);
});

test('registerAccount returns missing-credentials without inserting', async () => {
	let inserted = false;
	const result = await registerAccount({ username: 'ada', password: '' }, async () => {
		inserted = true;
	});

	assert.deepEqual(result, { status: 'missing-credentials' });
	assert.equal(inserted, false);
});

test('registerAccount inserts normalized credentials', async () => {
	let insertion;
	const result = await registerAccount(
		{ username: '  ada ', password: 'secret' },
		async (sql, params) => {
			insertion = { sql, params };
		}
	);

	assert.equal(insertion.sql, 'INSERT INTO accounts (username, password) VALUES (?, ?)');
	assert.deepEqual(insertion.params, ['ada', 'secret']);
	assert.deepEqual(result, { status: 'created', username: 'ada' });
});

test('registerAccount reports a duplicate username', async () => {
	const result = await registerAccount({ username: 'ada', password: 'secret' }, async () => {
		const error = new Error('Duplicate username');
		error.code = 'SQLITE_CONSTRAINT';
		throw error;
	});

	assert.deepEqual(result, { status: 'username-taken' });
});

test('registerAccount propagates unexpected database errors', async () => {
	const failure = new Error('Database unavailable');

	await assert.rejects(
		registerAccount({ username: 'ada', password: 'secret' }, async () => {
			throw failure;
		}),
		failure
	);
});

test('initializeDatabase creates the table and leaves the current schema unchanged', async () => {
	const statements = [];
	await initializeDatabase(
		async (sql) => statements.push(sql),
		async () => [{ name: 'username' }, { name: 'password' }]
	);

	assert.deepEqual(statements, [
		'CREATE TABLE IF NOT EXISTS accounts (username TEXT PRIMARY KEY, password TEXT NOT NULL)'
	]);
});

test('initializeDatabase migrates a schema with extra columns', async () => {
	const statements = [];
	await initializeDatabase(
		async (sql) => statements.push(sql),
		async () => [
			{ name: 'username' },
			{ name: 'password' },
			{ name: 'legacy_field' }
		]
	);

	assert.deepEqual(statements, [
		'CREATE TABLE IF NOT EXISTS accounts (username TEXT PRIMARY KEY, password TEXT NOT NULL)',
		'BEGIN TRANSACTION',
		'CREATE TABLE accounts_new (username TEXT PRIMARY KEY, password TEXT NOT NULL)',
		'INSERT INTO accounts_new (username, password) SELECT username, password FROM accounts',
		'DROP TABLE accounts',
		'ALTER TABLE accounts_new RENAME TO accounts',
		'COMMIT'
	]);
});

test('initializeDatabase rejects a schema without required columns', async () => {
	await assert.rejects(
		initializeDatabase(async () => {}, async () => [{ name: 'legacy_field' }]),
		{ message: 'The accounts table must contain username and password columns.' }
	);
});

test('initializeDatabase rolls back when migration fails', async () => {
	const statements = [];
	const failure = new Error('Migration failed');

	await assert.rejects(
		initializeDatabase(
			async (sql) => {
				statements.push(sql);
				if (sql === 'DROP TABLE accounts') {
					throw failure;
				}
			},
			async () => [{ name: 'username' }, { name: 'password' }, { name: 'legacy_field' }]
		),
		failure
	);

	assert.equal(statements.at(-1), 'ROLLBACK');
});
