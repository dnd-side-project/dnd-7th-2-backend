#!/bin/bash

REPOSITORY=/home/ec2-user/app/deploy
APP_NAME=niceteam-backend
DEPLOY_LOG=$REPOSITORY/$APP_NAME-deploy.log

cd $REPOSITORY

JAR_NAME=$(ls $REPOSITORY/*.jar | tail -n 1)

echo "> JAR_NAME :  $JAR_NAME" >> $DEPLOY_LOG

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 실행중인 애플리케이션 없음" >> $DEPLOY_LOG
else
  echo "> $CURRENT_PID 종료" >> $DEPLOY_LOG
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_NAME 실행 권한 추가" >> $DEPLOY_LOG

sudo chmod +x $JAR_NAME

echo "> 애플리케이션 배포" >> $DEPLOY_LOG

nohup java -jar \
  -Dspring.profiles.active=dev \
  $JAR_NAME >> $DEPLOY_LOG 2>&1 &

echo "> 배포 완료" >> $DEPLOY_LOG