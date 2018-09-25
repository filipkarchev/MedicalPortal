<?php
header("Content-Type: application/json");
require __DIR__ . "/../init.php";


$db = DB::getInstance()->_pdo;

//Check if there are any POST parameters
if (empty($_POST)) {
    isokFalse("No post data");
    die();
}

try{
$category=$_POST["category"];
$city=$_POST["city"];

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
    if(empty($_POST["city"]))
    {
       $stmt = $db->prepare("SELECT * FROM doctors WHERE `category` = ? AND `license` = 1");
       $stmt->execute(array($category)); 
    }
    else
    {
       $stmt = $db->prepare("SELECT * FROM doctors WHERE `category` = ? AND `license` = 1 AND `city` = ?");
       $stmt->execute(array($category,$city)); 
    }
    
    $info = $stmt->fetchAll(PDO::FETCH_ASSOC);

isokTrue($info);

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
