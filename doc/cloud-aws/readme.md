# Cloud - AWS

This section shows how to create a Kafka serverless cluster on AWS using terraform. This configuration is just for demo purpose.
If you use it in production, don't forget to change the public/private SSH keys `aws_key`.

Start by initializing your terraform project. Make sure that your AWS credentials are saved in `~/.aws/credentials`.

````
terraform init
````

Apply the configuration to your account. Don't forget to destroy once you have finished testing, otherwise you may be charged. The address of the EC2 machine
will be output on the terminal.
````
terraform apply
````

You can now use this EC2 address to connect to your machine (replace `[EC2_INSTANCE]`):
`````
ssh -i aws_key ubuntu@[EC2_INSTANCE]

`````

Go in the MSK console in your AWS administration console and copy paste the address of the bootstrap server (in client information). 
The address ends with port 9098. Replace `[BOOTSTRAP_SERVER]` accordingly.

````
export MSK_BOOTSTRAP_SERVER=[BOOTSTRAP_SERVER]
````

List topics and create a new `test` topic:
````
kafka-topics.sh --bootstrap-server $MSK_BOOTSTRAP_SERVER --command-config /var/lib/kafka_2.13-3.3.2/config/client.properties --list
kafka-topics.sh --bootstrap-server $MSK_BOOTSTRAP_SERVER --command-config /var/lib/kafka_2.13-3.3.2/config/client.properties --create --topic test --partitions 3 --replication-factor 1
````

You can now produce and receive message (open two terminals):
````
kafka-console-producer.sh --bootstrap-server $MSK_BOOTSTRAP_SERVER --producer.config /var/lib/kafka_2.13-3.3.2/config/client.properties --topic test
kafka-console-consumer.sh --bootstrap-server $MSK_BOOTSTRAP_SERVER --consumer.config /var/lib/kafka_2.13-3.3.2/config/client.properties --topic test --from-beginning
````


## Links

- [Create VPC using Terraform](https://linuxhint.com/create-aws-vpc-using-terraform/)
- [MSK Configuration using Terraform](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/msk_configuration)
- [MSK Serverless](https://itnext.io/getting-started-with-msk-serverless-and-aws-lambda-using-go-3d5acec6f99f)
- [IAM with MSK](https://blog.devops.dev/how-to-use-iam-auth-with-aws-msk-a-step-by-step-guide-2023-eb8291781fcb)
- [Terraform EC2 Script](https://www.middlewareinventory.com/blog/terraform-aws-ec2-user_data-example/)
- [MSK Create a Topic](https://docs.aws.amazon.com/msk/latest/developerguide/mkc-create-topic.html)
- [Attach role to EC2](https://skundunotes.com/2021/11/16/attach-iam-role-to-aws-ec2-instance-using-terraform/)