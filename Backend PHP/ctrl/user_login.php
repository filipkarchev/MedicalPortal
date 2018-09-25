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
$type = $_POST["type"];


if (empty($_POST["type"])) {
    isokFalse("No type data");
    die();
}

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
    //Get correct user
    if($type ==1)
    {
            $stmt = $db->prepare("SELECT users.*,"
                    . "(SELECT count(*) from messages where messages.receiver_id = users.id AND seen = 0 AND type = 2) as messages"
                    . " FROM users WHERE `username` = ? AND `password` = ?");
    }
    else
     {
            $stmt = $db->prepare("SELECT doctors.*,"
                    . "(SELECT count(*) from messages where messages.receiver_id = doctors.id AND seen = 0 AND type = 1) as messages,"
                    . "(SELECT count(*) from consultations where consultations.doctor_id = doctors.id AND seen = 0) as consultations"
                    . " FROM doctors WHERE `username` = ? AND `password` = ?");
    }
    
    
    
    $stmt->execute(array($username, $pass));
    
    //Check if there is user with this credentials
    if ($stmt->rowCount() == 0) {
        isokFalse("User doesn't exists");
        die();
}

    $info = $stmt->fetchAll(PDO::FETCH_ASSOC);

isokTrue($info);

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
