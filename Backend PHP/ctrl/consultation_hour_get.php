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
$date=$_POST["date"];

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
   
       $stmt = $db->prepare("SELECT * FROM consultations WHERE `doctor_id` = ? AND `date` = ?");
       $stmt->execute(array($doctor_id,$date)); 
    
    $info = $stmt->fetchAll(PDO::FETCH_ASSOC);

isokTrue($info);

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
