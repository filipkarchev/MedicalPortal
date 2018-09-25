<?php

class DB {
    
    private static $_instance = NULL;
    
    private function __construct() {
        try {
            $this->_pdo = new PDO('mysql:host=' . Config::get('mysql.host') . ';dbname=' . Config::get('mysql.db'), Config::get('mysql.user'), Config::get('mysql.pass'));
            $this->_pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->_pdo->exec("SET CHARACTER SET utf8");
        } catch (PDOException $e) {
            error_log($e->getMessage());
            die(Config::get("err_msg.sql_error"));
        }
    }
    
    public static function getInstance() {
        if (!isset(self::$_instance)) {
            self::$_instance = new DB();
        }
        return self::$_instance;
    }
}