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


}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
   
    $stmt = $db->prepare("SELECT * FROM messages WHERE `sender_id` = ? OR `receiver_id` = ?");
    $stmt->execute(array($user_id,$user_id));

    $info = $stmt->fetchAll(PDO::FETCH_ASSOC);

isokTrue($info);

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
