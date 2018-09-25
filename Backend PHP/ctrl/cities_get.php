<?php
header("Content-Type: application/json");
require __DIR__ . "/../init.php";


$db = DB::getInstance()->_pdo;

try{
$category=$_POST["category"];

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}
try{
    
    $stmt = $db->prepare("SELECT city FROM doctors WHERE `category` = ?   GROUP BY city");
    $stmt->execute(array($category));

    $info = $stmt->fetchAll(PDO::FETCH_ASSOC);

isokTrue($info);

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
