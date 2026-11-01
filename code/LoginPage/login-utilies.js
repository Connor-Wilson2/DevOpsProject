function getCredentials(body) {
	const username = typeof body.username === 'string' ? body.username.trim() : '';
	const password = typeof body.password === 'string' ? body.password : '';

	return username && password ? { username, password } : null;
}

async function initializeDatabase(run, all) {
	await run(
		'CREATE TABLE IF NOT EXISTS accounts (username TEXT PRIMARY KEY, password TEXT NOT NULL)'
	);

	const columns = await all('PRAGMA table_info(accounts)');
	const columnNames = columns.map((column) => column.name);
	if (columnNames.length === 2 && columnNames[0] === 'username' && columnNames[1] === 'password') {
		return;
	}
	if (!columnNames.includes('username') || !columnNames.includes('password')) {
		throw new Error('The accounts table must contain username and password columns.');
	}

	await run('BEGIN TRANSACTION');
	try {
		await run(
			'CREATE TABLE accounts_new (username TEXT PRIMARY KEY, password TEXT NOT NULL)'
		);
		await run(
			'INSERT INTO accounts_new (username, password) SELECT username, password FROM accounts'
		);
		await run('DROP TABLE accounts');
		await run('ALTER TABLE accounts_new RENAME TO accounts');
		await run('COMMIT');
	} catch (err) {
		await run('ROLLBACK');
		throw err;
	}
}

async function authenticateAccount(body, get) {
	const credentials = getCredentials(body);
	if (!credentials) {
		return { status: 'missing-credentials' };
	}

	const account = await get(
		'SELECT username FROM accounts WHERE username = ? AND password = ?',
		[credentials.username, credentials.password]
	);
	if (!account) {
		return { status: 'invalid-credentials' };
	}

	return { status: 'authenticated', username: account.username };
}

async function registerAccount(body, run) {
	const credentials = getCredentials(body);
	if (!credentials) {
		return { status: 'missing-credentials' };
	}

	try {
		await run(
			'INSERT INTO accounts (username, password) VALUES (?, ?)',
			[credentials.username, credentials.password]
		);
		return { status: 'created', username: credentials.username };
	} catch (err) {
		if (err.code === 'SQLITE_CONSTRAINT') {
			return { status: 'username-taken' };
		}
		throw err;
	}
}

module.exports = {
	authenticateAccount,
	getCredentials,
	initializeDatabase,
	registerAccount
};
