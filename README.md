This is for Microsoft Engage Programme trying to build something awesome and learn at the same time. Some of the amazing things. 
# College Buddy

An application that gives students an array of digital academic and social tools to stay engaged with their studies, peers and broader university community during pandemic.

## Features

- Use [**Collab**](#collab) to find a perfect partner for working on a project, prepare for an exam and much more. 
- With the help of [**Bunk Manager**](#bunk-manager) you can manage your attendence across all subjects. 
- [**Live Events**](#live-events) lets you enjoy college life by keeping you updated with all the fun and educative events across the campus. 
- You can pariticipate in all the happening chat in the college with the help of [**Group Chat**](#group-chat) 
<!-- - Pinch to Scale and Rotate views.
- [**Deleting**](#deleting) Views
- [**Saving**](#saving) Video after editing. -->


## Collab
We can apply inbuild filter to the source images using 

 `mPhotoEditor.setFilterEffect(PhotoFilter.BRIGHTNESS);`

![GIF-210301_210700](https://user-images.githubusercontent.com/56435229/109521172-070e5500-7ad3-11eb-9b42-1252783b5c30.gif)

We can also apply custom effect using `Custom.Builder`



## Bunk Manager



We can add the text with inputText and colorCode like this

`mPhotoEditor.addText(inputText, colorCode);` 

![GIF-210301_205915](https://user-images.githubusercontent.com/56435229/109520629-75064c80-7ad2-11eb-846c-e0bf9a579d46.gif)


In order to edit the text we need the view, which we will receive in our PhotoEditor callback. This callback will trigger when we **Long Press** the added text

 ```java
 mPhotoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(View rootView, String text, int colorCode) {
                
            }
        });
  ```



## Live Events

![GIF-210301_204946](https://user-images.githubusercontent.com/56435229/109518071-ef819d00-7acf-11eb-84d5-9edbe3e06088.gif)


We can add the Emoji by `PhotoEditor.getEmojis(getActivity());` which will return a list of emojis unicode. You can also long press on the added emoji to edit them.




## Group Cha
 We need to provide a Bitmap to add our Images  `mPhotoEditor.addImage(bitmap);`
 
 
 



## Deleting
  For deleting a Text/Emoji/Image we can click on the view to toggle the view highlighter box which will have a close icon. So, by clicking on the icon we can delete the view.
  
  
  

## Saving
   
   We need to provide a file with callback method when edited image is saved
   
   ```java
    mPhotoEditor.saveAsFile(filePath, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                       Log.e("PhotoEditor","Image Saved Successfully");
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("PhotoEditor","Failed to save Image");
                    }
                });
```
![GIF-210301_210037](https://user-images.githubusercontent.com/56435229/109521035-dfb78800-7ad2-11eb-9cad-6a9d641b96b1.gif)
