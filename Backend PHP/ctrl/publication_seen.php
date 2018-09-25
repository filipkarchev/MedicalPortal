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
$publication_id=$_POST["publication_id"];

$stmtUpdate = $db->prepare("UPDATE publications SET seen = seen + 1 WHERE id= ?"); 
$stmtUpdate->execute(array($publication_id));

isokTrue("success");

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
