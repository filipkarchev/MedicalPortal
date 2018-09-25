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
$creator_id=$_POST["creator_id"];
$comments=$_POST["comment"];

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
    
     $stmt = $db->prepare("INSERT INTO comments(`comment`,`creator_id`,`publication_id`) VALUES(?, ?, ?)");
     $stmt->execute(array($comments,$creator_id,$publication_id));
    
//     $stmt_update = $db->prepare("INSERT INTO comments(`comment`,`creator_id`,`publication_id`) VALUES(?, ?, ?)");
//     $stmt_update->execute(array($publication_id));
     
isokTrue("comment added");

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
