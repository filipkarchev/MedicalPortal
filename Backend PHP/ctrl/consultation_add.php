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
$doctor_id=$_POST["doctor_id"];
$user_id=$_POST["user_id"];
$date=$_POST["date"];
$hour=$_POST["hour"];

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
    
     $stmt = $db->prepare("INSERT INTO consultations(`user_id`,`doctor_id`,`date`,`hour`) VALUES(?, ?, ?, ?)");
     $stmt->execute(array($user_id,$doctor_id,$date,$hour));
    

isokTrue("consultation added");

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
