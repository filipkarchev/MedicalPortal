<?php
header("Content-Type: application/json");
require __DIR__ . "/../init.php";


$db = DB::getInstance()->_pdo;

//Check if there are any POST parameters
if (empty($_POST["image"])) {
    isokFalse("No image");
    die();
}

try{
$image=$_POST["image"];
$image_name=$_POST["name"];
$type=$_POST["type"];
$id=$_POST["id"];

}  catch (Exception $e){
    isokFalse("Wrong post data");
    die();
}

try{
    $decoded_string = base64_decode($image);
    $path = getenv("DOCUMENT_ROOT")."/MedicalPortal/ctrl/images/".$image_name;
    $savePath="images/".$image_name;
  //  echo $path;
    $file = fopen($path, "wb");
    
    $is_written = fwrite($file, $decoded_string);
    fclose($file);
    
    if($is_written<=0)
    {
        isokFalse("no written");
        die();
    }
    
  if($type==1)
  {
       $stmt = $db->prepare("UPDATE `medical_portal_backend`.`doctors` SET `icon_url`= ? WHERE `id`=?");
       $stmt->execute(array($savePath,$id));
  }
 else {
       $stmt = $db->prepare("UPDATE `medical_portal_backend`.`users` SET `icon_url`= ? WHERE `id`=?");
       $stmt->execute(array($savePath,$id));
  }

isokTrue("$savePath");

} catch(PDOException $ex) {
    //Something went wrong rollback!
   // $db->rollBack();
    isokFalse($ex->getMessage());
}
