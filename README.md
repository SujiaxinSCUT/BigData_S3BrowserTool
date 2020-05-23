# 5月21日 星期四 

分化模块降低主要工作代码的耦合度

增加了连接S3Browser后的一些操作方法

# 5月22日 星期五

增加遍历目录下所有文件方法，获取其更新时间，与桶中对象进行对比，决定是否上传

增加上传大文件方法，并将上传信息进行缓存，当程序意外中断后，根据缓存进行断点重传

# 5月23日 星期六

增加一个队列任务类，将需要上传和删除的文件列表放入其中，由其执行上传删除文件操作，大文件的上传使用多线程处理，不影响其他普通文件的上传和删除

完善遍历同步目录下所有文件后，并对比文件更新时间后，将决定需要上传和删除的文件放置入任务队列中

至此已完成该文件同步器的基本核心功能，选定同步目录和桶，对其中的所有文件进行单向同步

实现一个基本的GUI界面，输入密钥向同步网点进行连接，将同步操作可视化

![image.png](https://upload-images.jianshu.io/upload_images/17501422-d89be964136dab7a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/17501422-531e0170363bad24.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](https://upload-images.jianshu.io/upload_images/17501422-c4a398671ba564b8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)