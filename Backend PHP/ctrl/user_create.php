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
//Get POST parameters for user_id,user_name and picture_url
$username=$_POST["username"];
$pass=$_POST["password"];
$firstname=$_POST["firstname"];
$lastname=$_POST["lastname"];
$phone=$_POST["phone"];
}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{

    $stmt = $db->prepare("INSERT INTO users(`username`,`password`,`firstname`,`lastname`,`phone`) VALUES(?, ?, ?, ?, ?)");
    $stmt->execute(array($username, $pass,$firstname,$lastname,$phone));

isokTrue("User created");
   

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
