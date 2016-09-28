//download image and show progress while downloading------using download manager
public void downloadImage(){
String urlDownload = images;
DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlDownload));

    request.setDescription("HOM");
    request.setTitle("Download");
    request.allowScanningByMediaScanner();
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "test.jpg");
    final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    final long downloadId = manager.enqueue(request);
    new Thread(new Runnable() {
        @Override
        public void run() {
             downloading = true;
            while (downloading) {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadId);
                Cursor cursor = manager.query(q);
                cursor.moveToFirst();
                int bytes_downloaded = cursor.getInt(cursor
                        .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;
                    filePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                }
                final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(dl_progress==100){
                            progressBar.setProgress((int) dl_progress);
                            progress_text.setText(String.valueOf(dl_progress)+" %");
                            showImage(filePath);
                        }else {
                            progressBar.setProgress((int) dl_progress);
                            progress_text.setText(String.valueOf(dl_progress)+" %");
                        }
                    }
                });
                cursor.close();
            }
        }
    }).start();
}
public void showImage(String file_path){
    progress_text.setVisibility(View.GONE);
    progressBar.setVisibility(View.GONE);
    File imgFile = new  File(file_path);
    if(imgFile.exists()){
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imgDisplay.setImageBitmap(myBitmap);
    }
}