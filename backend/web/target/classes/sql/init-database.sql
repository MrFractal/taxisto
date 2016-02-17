REPLACE INTO account SET id = -1, email = 'admin@propertyminder.com', password = 'admin';
REPLACE INTO security_roles SET account_id = -1, roles = 'ROLE_ADMIN_SUPER';
REPLACE INTO security_roles SET account_id = -1, roles = 'ROLE_ADMIN';