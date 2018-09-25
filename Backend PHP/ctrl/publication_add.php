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
$title=$_POST["title"];
$description=$_POST["description"];
$creator_id=$_POST["creator_id"];
$date=$_POST["date"];
$category=$_POST["category"];

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
    
     $stmt = $db->prepare("INSERT INTO publications(`creator_id`,`title`,`description`,`data`,`category`) VALUES(?, ?, ?, ?, ?)");
     $stmt->execute(array($creator_id,$title,$description,$date,$category));
    

isokTrue("publication added");

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
