//log the start of the script execution
print('START');

//switch to the 'product-service' database (or create it if it doesn't exist)
db = db.getSiblingDB('product-service');

db.createUser(
    {
        user: 'mongoadmin',
        pwd: 'password',
        roles: [{role: 'readWrite', db: 'product-service'}],
    }
);

//create the 'product' collection in the 'product-service' database
db.createCollection('user');

//log the end of the script execution
print('END');