# AWS

## Apr 10 - Day 01

### 1. AWS CLI 설치 - Windows

```bash
# 설치 명령어
msiexec.exe /i https://awscli.amazonaws.com/AWSCLIV2.msi

# 설치 확인
aws --version
# aws-cli/2.34.28 Python/3.14.3 Windows/11 exe/AMD64
```

### 2. AWS CLI 설치 - Linux

```bash
# 패키지 다운로드
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"

# 압축 해제
unzip awscliv2.zip

# 설치 진행
sudo ./aws/install
```

### 3. 접속 정보 설정

```bash
aws configure
# AWS Access Key ID [None]: ACCESS_KEY
# AWS Secret Access Key [None]: SECRET_KEY
# Default region name [None]: ap-northeast-2
# Default output format [None]: json
```

### 4. 예시 명령어

```bash
# 현재 설정 확인
aws configure list

# 현재 계정 확인
aws sts get-caller-identity

# IAM & 권한 확인 (현재 권한 테스트)
aws iam list-users

# 인스턴스 조회
aws ec2 describe-instances

# 특정 상태만 보기
aws ec2 describe-instances --query 'Reservations[].Instances[].InstanceId'

# 실행중인 인스턴스 확인
aws ec2 describe-instances --filters Name=instance-state-name,Values=running --query "Reservations[].Instances[].InstanceId" --output text

# 인스턴스 시작/중지/삭제
aws ec2 start-instances --instance-ids i-xxxx
aws ec2 stop-instances --instance-ids i-xxxx
aws ec2 terminate-instances --instance-ids i-xxxx
```

## Apr 13 - Day 02

### 1. On-Premise 장비에 SSM Agent 설치

```bash
# Snap으로 설치
sudo snap install amazon-ssm-agent --classic

# 실행 중인지 확인
sudo snap list amazon-ssm-agent

# 실행
sudo snap start amazon-ssm-agent

# 서비스 상태 확인
sudo snap services amazon-ssm-agent
```

## Apr 14 - Day 03

### 1. DynamoDB in AWS CLI

```bash
# 테이블 목록 조회
aws dynamodb list-tables

# 테이블 생성
aws dynamodb create-table \
  --table-name ce-27-fisa \
  --attribute-definitions AttributeName=id,AttributeType=S \
  --key-schema AttributeName=id,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST

# 테이블 생성 완료 대기
aws dynamodb wait table-exists --table-name ce-27-fisa

# 테이블 생성 확인
aws dynamodb describe-table --table-name ce-27-fisa

# 항목 insert
aws dynamodb put-item --table-name ce-27-fisa --item '{"id": {"S": "123"}}'

# 항목 insert (정상 실행 여부 확인)
aws dynamodb put-item --table-name ce-27-fisa --item '{"id": {"S": "123"}, "name": {"S": "itemName"}}'

# 항목 조회 
aws dynamodb get-item --table-name ce-27-fisa --key '{"id": {"S": "123"}}'

# 항목 업데이트
aws dynamodb update-item \
  --table-name ce-27-fisa --key '{"id": {"S": "123"}}' \
  --update-expression "SET #n = :name" \
  --expression-attribute-names '{"#n": "name"}' \
  --expression-attribute-values '{":name": {"S": "newItemName"}}'

# 항목 삭제
aws dynamodb delete-item --table-name ce-27-fisa --key '{"id": {"S": "123"}}'

# 테이블 삭제
aws dynamodb delete-table --table-name ce-27-fisa
```

## Apr 15 - Day 04

## Apr 16 - Day 05

### 1. Stress-ng

```bash
# 레포지토리 업데이트
sudo apt update && sudo apt upgrade -y

# stress-ng 설치
sudo apt install -y stress-ng

# core 개수 확인
grep -c processor /proc/cpuinfo
# 1

# 1코어의 CPU를 60초간 과부하로 설정, 70% 사용량으로 test
stress-ng --cpu 1 --cpu-load 70 --timeout 60s --metrics
```

## Apr 17 - Day 06

### 1. Install EKS

```bash
# 파일 다운로드 및 압축 풀기
curl --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp

# 바이너리 이동
sudo mv /tmp/eksctl /usr/local/bin

# 설치 확인
eksctl version
```

## Apr 20 - Day 07

### 1. EKS

```bash
# 클러스터 버전 확인
aws eks describe-cluster-versions

# 클러스터 생성
eksctl create cluster \
  --name ce-27-cluster\
  --version 1.35 \
  --nodes=3 \
  --node-type=t2.small \
  --region ap-northeast-2

 # 클러스터 상태 확인
eksctl get cluster

# kubectl 연결 확인
kubectl get nodes

# 설정 확인
kubectl config get-contexts

# 현재 연결된 클러스터 확인  
kubectl config current-context

# 클러스터 삭제
eksctl delete cluster ce-27-cluster
```

### 2. Mission

```bash
# SpringBoot 빌드
.mvnw clean install

# ECR 로그인
aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin <URL-1>

# 도커 이미지 빌드
docker build -t ce-27-springboot-eks .

# 도커 이미지 태그
docker tag ce-27-springboot-eks:latest <URL-2>/ce-27-springboot-eks:latest

# 도커 이미지 푸시
docker push <URL-2>/ce-27-springboot-eks:latest

# 클러스터 생성
eksctl create cluster \
  --name ce-27-cluster \
  --version 1.35 \
  --nodes=3 \
  --node-type=t2.small \
  --region ap-northeast-2

# 로컬에서 kubeconfig 사용 설정
aws eks --region ap-northeast-2 update-kubeconfig --name ce-27-cluster

# YAML 작성 후 배포
kubectl apply -f k8s.yaml

# 상태 확인
kubectl get svc
kubectl get deployment
kubectl get pods

# 클러스터 삭제
eksctl delete cluster ce-27-cluster
```

## Apr 21 - Day 08

### 1. EKS Node

```bash
# 클러스터 생성
eksctl create cluster \
  --name ce-27-cluster\
  --version 1.35 \
  --nodes=1 \
  --node-type=t2.small \
  --region ap-northeast-2

# 노드 수 증가
eksctl scale nodegroup \
  --cluster ce-27-cluster \
  --name ng-ae50e1a5 \
  --nodes 3 \
  --nodes-min 3 \
  --nodes-max 3 \
  --region ap-northeast-2

# 클러스터 삭제
eksctl delete cluster ce-27-cluster
```