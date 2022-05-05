Java DynamoDB Local with Sqlite4java Demo
=========================================

试过 https://cashapp.github.io/tempest/guide/testing/，但是还是需要自己处理 sqlite4java native，并没有简单多少。

最后还是采用 https://stackoverflow.com/a/37780083/342235 这里的办法，也没有麻烦多少。

需要注意的是：DynamoDBLocal中引用的aws-java-sdk-core版本是旧的，与指定的aws-java-sdk-dynamodb版本不同时， 会报一种Field错误，需要手动处理版本。
可以使用`aws-java-sdk-bom`来避免版本问题，但需要在`DynamoDBLocal`中`exclusion`掉不匹配的库

这种办法与docker相比，速度快了很多。

Run:

```
mvn test
```