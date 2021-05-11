module.exports = {
    "plugins": ["es"],
    "env": {
        "browser": true,
        "es6": true,
        "jquery": true
    },
    "parserOptions": {
        "ecmaVersion": 6,
        "sourceType": "module"
    },
    //for ie11-compatibility, can be removed once we drop ie11 support
    "extends": [
        "plugin:es/no-new-in-es2015"
    ],
    "rules": {
        //modules are resolved by webpack, so we can use them in our code
        "es/no-modules": "off",
        //ie11 does support block-scoped variables, so we can use them
        "es/no-block-scoped-variables": "off"
    }
};
