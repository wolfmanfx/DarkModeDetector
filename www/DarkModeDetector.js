var exec = require('cordova/exec');

exports.getState = function(success, error) {
    exec(success, error, 'DarkModeDetector', 'getState', []);
};