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
$user_id=$_POST["user_id"];
$doctor_id=$_POST["doctor_id"];

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
    if(empty($_POST["user_id"]))
    {
       $stmt = $db->prepare("SELECT consultations.id,consultations.user_id,consultations.doctor_id,consultations.date,consultations.hour,doctors.firstname,doctors.lastname,doctors.city,doctors.address FROM consultations LEFT JOIN doctors ON(consultations.doctor_id = doctors.id) WHERE `doctor_id` = ? order by date,hour");
       $stmt->execute(array($doctor_id)); 
    }
    else
    {
       $stmt = $db->prepare("SELECT consultations.id,consultations.user_id,consultations.doctor_id,consultations.date,consultations.hour,doctors.firstname,doctors.lastname,doctors.city,doctors.address FROM consultations LEFT JOIN doctors ON(consultations.doctor_id = doctors.id) WHERE `user_id` = ? order by date,hour");
       $stmt->execute(array($user_id)); 
    }
    
    $info = $stmt->fetchAll(PDO::FETCH_ASSOC);

isokTrue($info);

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
