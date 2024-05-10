# 我需要做到的是什么？

我需要做一个HTTP服务器，从宏观来看我需要实现的最主要的功能是什么？

> 接收客户端的http请求，解析并返回资源和信息给客户端

# 在第一个版本我要做到什么？

在一个版本中，我不考虑使用socket与多线程，我会读取我硬盘中的文件，文件的内容写了HTTP请求内容，包含完整的请求行、请求头、空行、请求体

**目标：当解析完httpRequest对象后，在本机输出“xxx解析完成”，并输出解析的内容**

1. 我会通过字符流FileRead去读取文件，获取FileRead对象，然后再使用字节缓冲流BufferedRead读取FileRead对象
2. 我会使用Stringbuilder去存储http请求的原始内容
3. 创建httpRequest对象
4. 调用三个方法，分别是：解析请求行、解析请求头、解析请求体，将相应的属性写入httpRequest

> 第一版于2024年4月24日19:58分编写完毕

# 在第二个版本我要做到什么？

现在已经实现了第一个版本，解析HTTP请求，在第二个版本中，我要完成响应HTTP请求，此版本任然使用本地文件作为HTTP请求来源

**目标：当接收到了HTTP请求时，在本机输出Hello**

1. 定义好响应体对象，包括响应行、响应头、响应体
2. 定义好生成响应体的方法

> 2024年4月24日21:00:33完成第二版本的编写

# 在第三个版本我要做到什么？

第二个版本的生成响应体方法，是不太优雅；纯纯是获取数据并输出在本地，在第三个版本中要使用socket启动服务进行监听，并在本机控制台打印出HTTP请求

目标：从浏览器访问localhost:9090,本机打印请求体的内容

1. 使用Scoket监听9090端口
2. 将本地文件IO替换为字节流IO

> 2024年4月24日21:57:34完成第三版本的编写

## 遇到的困难

http发送了，但是请求头是null

问题原因：http请求可能有请求体，也可能没有请求体；在处理请求头的代码中，我只写了判断有请求体时的代码，导致第22行的代码不会被运行，在第26加上了设置请求头的语句，在两种情况下都会设置请求头；只不过在有请求体的情况下，请求体会被设置两次，不太优雅。

```java
private static void decodeRequestHeader(StringBuilder httpRcontent,Request request) throws IOException{
        //创建一个Map对象，用于存储请求头的信息,并初始化容量
        HashMap<String,String> headers = new HashMap<>(16);
        //获取请求的原始数据，解析为字符串数组
        String[] httpContent = httpRcontent.toString().split("\n");

        //读取请求头信息，每行都是一个键值对，以空行作为请求头的标准
        String[]kv;//键值对key:values
        int tag=0;
        for(String line:httpContent){
            line = line.trim();//去除字符串两端的空格
            //判断是否为请求头信息
            tag++;
            if (tag == 1){continue;}

            if(!line.equals("")){
                kv = line.split(":");
                headers.put(kv[0],kv[1]);

            }else {//请求有请求体的情况
                //设置请求对象中的请求头信息
                request.setHeaders(headers);
                break;
            }
            //请求没有请求体的情况
            request.setHeaders(headers);
        } //for end
        System.out.println("请求头解析完成");
        System.out.println(request.getHeaders());
    }
```

# 在第四个版本我要做到什么？

第三个版本耽误了好长时间，但是结果终归是好的；对于上个版本出现的问题来自于对http请求和代码读取的不熟练，这次遇到了，下次就记住了。在第四个版本我要做到对客户端返回数据

**目标：客户端请求服务器，服务器通过返回Hello HTTP Client给网页**

1. 使用BufferedWriter缓冲流向客户端发送数据
2. 在通信完关闭Socket连接

> 2024年4月25日01:02:45第四个版本完成

## 尝试的事&发现的问题

我在看响应头相关的属性时，发现了`Location`这个属性，可以让客户端重定向；我试了不行，最后查到的是，状态码也要进行对应的变化

如果要进行重定向，状态码需要为301 or 302

参考资料：[HTTP 状态码 | 菜鸟教程 (runoob.com)](https://www.runoob.com/http/http-status-codes.html)

| 分类 | 分类描述                                       |
| ---- | ---------------------------------------------- |
| 1**  | 信息，服务器收到请求，需要请求者继续执行操作   |
| 2**  | 成功，操作被成功接收并处理                     |
| 3**  | 重定向，需要进一步的操作以完成请求             |
| 4**  | 客户端错误，请求包含语法错误或无法完成请求     |
| 5**  | 服务器错误，服务器在处理请求的过程中发生了错误 |

## 遇到的困难（未解决）

在下列代码第9行，我创建了缓冲流进行读取文件，在第26的方法中，我创建了缓冲流用于返回响应体

在第13行的代码中，如果while中的循环条件为while((line = bufferedReader.readLine()) != null),bufferedWriter就无法给客户端正常返回，只有客户端完全断开连接才能发送出去，代码中已经进行了flush

```java
    public static void main(String[] args) throws IOException {
        //服务器持续监听9090端口
        ServerSocket serverSocket = new ServerSocket(9090);
        while(true) {
            try {
                Socket httpSocket = serverSocket.accept();

                //创建缓冲流进行读取文件
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpSocket.getInputStream()));
                //创建StringBuilder类接收http请求内容
                StringBuilder httpRequestContent = new StringBuilder();
                String line = "";
                while (!((line = bufferedReader.readLine()).isEmpty())) {//判断读取到的内容是否为空
                    //将读取到的内容添加到httpRequestContent中
                    httpRequestContent.append(line + '\n');//添加换行还原原始字符串
                }
                System.out.println(httpRequestContent.toString());

                //创建Request对象，用于存放解析后的请求信息
                Request request = new Request();
                //解析请求行
                decodeRequestLine(httpRequestContent, request);
                //解析请求头
                decodeRequestHeader(httpRequestContent, request);
                //解析请求体
                decodeRequestBody(httpRequestContent, request);

                //生成响应体
                generateResponse(request, httpSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
```

# 在第五个版本我需要干什么？

整体而言，代码有些混乱了，在这个版本我需要将路由做出来，所有我需要一个函数专门进行路由。除此之外，我还需要优化处理请求的函数

程序整体分为两个部分，一个处理请求，二是返回响应；除此之外，还需要有一个用来返回错误的函数，用来校验请求体中的信息，并返回对应的响应体

1. 处理请求

   1. 处理请求行
   2. 处理请求头
   3. 处理请求体

2. 返回响应

   1. 校验请求行的内容（uri，method）
      1. 路由转换
   2. 生成响应头
   3. 生成响应体

   > 在2024年4月26 14:00左右完成
   
   

# 积累

## URI 和 URL 的区别

> uniform: adj.完全一样的；不变的；同类的
>
> resource: n.资源
>
> identifier: n.鉴定人；鉴定物；标识符
>
> locator: n.（利用无线电信号工作的）定位器

### URI（Uniform Resource Identifier）:统一资源标识符

顾名思义，URI就是资源的标识符😂，URI是资源在服务器上唯一的标识，是相对地址

| POST /some/path.html HTTP/1.1 | /some/path.html |
| ----------------------------- | --------------- |

上方 /some/path.html 就是URI

### URL（Uniform Resource Locator）:统一资源定位符

URL是对于整个网络来说的，URL是一个完整的链接，就是域名，是可以直接进行访问的

## substring与lastIndexOf



# 备忘录

1. 响应时传输格式的属性
2. HashMap的初始化方式（匿名函数初始化）
3. git回滚操作
4. GET请求响应JSON
5. GET通过 url paramenters解析
6. POST JSON+响应JSON
7. GET请求图片
8. POST请求
9. FormData(主要用在上传文件)





# 













