<?php //error_reporting(E_ALL); ini_set('display_errors', 1);

defined('ROOT_PATH') ? null : define('ROOT_PATH', dirname(__FILE__));

require_once 'conf.php';
require_once 'func.php';

// libs
require_once 'libs/Configs.php';
require_once 'libs/DB.php';

date_default_timezone_set('Europe/Sofia');