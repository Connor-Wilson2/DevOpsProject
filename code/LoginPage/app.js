const path = require('path');
const crypto = require('crypto');
const sqlite3 = require('sqlite3').verbose();
const express = require('express');

const app = express();
const port = process.env.PORT || 8080;
const databasePath = path.join(__dirname, 'accounts.db');
const sessions = new Map();

app.set('view engine', 'ejs');
app.use(express.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

const db = new sqlite3.Database(
	databasePath,
	sqlite3.OPEN_READWRITE | sqlite3.OPEN_CREATE
);

function run(sql, params = []) {
	return new Promise((resolve, reject) => {
		db.run(sql, params, function (err) {
			if (err) {
				reject(err);
				return;
			}
			resolve(this);
		});
	});
}

function get(sql, params = []) {
	return new Promise((resolve, reject) => {
		db.get(sql, params, (err, row) => {
			if (err) {
				reject(err);
				return;
			}
			resolve(row);
		});
	});
}

function all(sql, params = []) {
	return new Promise((resolve, reject) => {
		db.all(sql, params, (err, rows) => {
			if (err) {
				reject(err);
				return;
			}
			resolve(rows);
		});
	});
}

async function initializeDatabase() {
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

function createSession(res, username) {
	const sessionId = crypto.randomBytes(32).toString('hex');
	sessions.set(sessionId, username);
	res.setHeader('Set-Cookie', `todo_app_session=${sessionId}; HttpOnly; SameSite=Lax; Path=/`);
}

function getSessionUsername(req) {
	const cookieHeader = req.headers.cookie || '';
	const match = cookieHeader.match(/(?:^|;\s*)todo_app_session=([a-f0-9]+)/);
	return match ? sessions.get(match[1]) : undefined;
}

app.get('/', (req, res) => {
	const username = getSessionUsername(req);
	if (!username) {
		return res.redirect('/login');
	}
	return res.render('page/index', { username });
});

app.get('/login', (req, res) => {
	res.render('page/login', { error: null });
});

app.post('/login', async (req, res) => {
	const username = typeof req.body.username === 'string' ? req.body.username.trim() : '';
	const password = typeof req.body.password === 'string' ? req.body.password : '';

	if (!username || !password) {
		return res.status(400).render('page/login', {
			error: 'Enter both your username and password.'
		});
	}

	try {
		const account = await get(
			'SELECT username FROM accounts WHERE username = ? AND password = ?',
			[username, password]
		);
		if (!account) {
			return res.status(401).render('page/login', {
				error: 'The username or password is incorrect.'
			});
		}
		createSession(res, account.username);
		return res.redirect('/');
	} catch (err) {
		console.error('Login failed:', err.message);
		return res.status(500).render('page/login', {
			error: 'Unable to log in right now. Please try again later.'
		});
	}
});

app.get(['/createaccount', '/create-account'], (req, res) => {
	res.render('page/createaccount', { error: null });
});

async function createAccount(req, res) {
	const username = typeof req.body.username === 'string' ? req.body.username.trim() : '';
	const password = typeof req.body.password === 'string' ? req.body.password : '';

	if (!username || !password) {
		return res.status(400).render('page/createaccount', {
			error: 'Enter both a username and password.'
		});
	}

	try {
		await run('INSERT INTO accounts (username, password) VALUES (?, ?)', [username, password]);
		createSession(res, username);
		return res.redirect('/');
	} catch (err) {
		if (err.code === 'SQLITE_CONSTRAINT') {
			return res.status(409).render('page/createaccount', {
				error: 'That username is already in use. Choose another username.'
			});
		}
		console.error('Account creation failed:', err.message);
		return res.status(500).render('page/createaccount', {
			error: 'Unable to create your account right now. Please try again later.'
		});
	}
}

app.post(['/createaccount', '/create-account', '/api/add_account'], createAccount);

initializeDatabase()
	.then(() => {
		app.listen(port, () => {
			console.log(`App listening on port ${port}!`);
		});
	})
	.catch((err) => {
		console.error('Unable to initialize the accounts database:', err.message);
		process.exitCode = 1;
	});
