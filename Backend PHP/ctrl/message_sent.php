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
$text=$_POST["text"];
$sender_id=$_POST["sender_id"];
$sender_name=$_POST["sender_name"];
$receiver_id=$_POST["receiver_id"];
$receiver_name=$_POST["receiver_name"];
$date=$_POST["date"];
$title=$_POST["title"];
$type=$_POST["type"];
$image=$_POST["image"];

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
    
    if(empty($_POST["image"]))
    {
     $stmt = $db->prepare("INSERT INTO messages(`text`,`title`,`sender_id`,`sender_name`,`receiver_id`,`receiver_name`,`date`,`type`) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
     $stmt->execute(array($text,$title,$sender_id,$sender_name,$receiver_id,$receiver_name, $date,$type));
    
    }
    else
    {
        
    $image_name_date = date("YmdHis");
    $decoded_string = base64_decode($image);
    $path = getenv("DOCUMENT_ROOT")."/MedicalPortal/ctrl/images/".$image_name_date.$sender_id.".png";
    $savePath="images/".$image_name_date.$sender_id.".png";
  //  echo $path;
    $file = fopen($path, "wb");
    
    $is_written = fwrite($file, $decoded_string);
    fclose($file);
    
    if($is_written<=0)
    {
        isokFalse("no written");
        die();
    }
    
    $stmt = $db->prepare("INSERT INTO messages(`text`,`title`,`sender_id`,`sender_name`,`receiver_id`,`receiver_name`,`date`,`type`,`image`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
    $stmt->execute(array($text,$title,$sender_id,$sender_name,$receiver_id,$receiver_name, $date,$type, $savePath));
   
    }
    
    

isokTrue("message is sent");

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
