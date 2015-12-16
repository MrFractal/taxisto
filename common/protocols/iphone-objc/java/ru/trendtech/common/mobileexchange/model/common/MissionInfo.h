#import "NSMutableArray.h"
#import "Date.h"
#import "NSMutableArray.h"

/**
 * Created by max on 06.02.14.
 */

@interface MissionInfo : NSObject {
  long id;
  Date * timeOfRequesting;
  Date * timeOfAssigning;
  Date * timeOfArriving;
  Date * timeOfSeating;
  Date * timeOfFinishing;
  Date * timeOfPayment;
  NSMutableArray * expectedArrivalTimes;
  NSMutableArray * options;
  char rating;
  BOOL fixedMission;
  NSString * addressFrom;
  NSString * addressTo;
  ItemLocation * locationFrom;
  ItemLocation * locationTo;
  int paymentType;
  float price;
  int autoType;
  NSString * comment;
  DriverInfo * driverInfo;
  ClientInfo * clientInfo;
}

@property(nonatomic) long id;
@property(nonatomic, retain) Date * timeOfRequesting;
@property(nonatomic, retain) Date * timeOfAssigning;
@property(nonatomic, retain) Date * timeOfArriving;
@property(nonatomic, retain) Date * timeOfSeating;
@property(nonatomic, retain) Date * timeOfFinishing;
@property(nonatomic, retain) Date * timeOfPayment;
@property(nonatomic, retain) NSMutableArray * expectedArrivalTimes;
@property(nonatomic) char rating;
@property(nonatomic, retain) NSString * addressFrom;
@property(nonatomic, retain) NSString * addressTo;
@property(nonatomic, retain) ItemLocation * locationFrom;
@property(nonatomic, retain) ItemLocation * locationTo;
@property(nonatomic) int paymentType;
@property(nonatomic) float price;
@property(nonatomic, retain) DriverInfo * driverInfo;
@property(nonatomic, retain) ClientInfo * clientInfo;
@property(nonatomic) int autoType;
@property(nonatomic, retain) NSString * comment;
@property(nonatomic, retain) NSMutableArray * options;
@property(nonatomic) BOOL fixedMission;
- (void) init;
- (void) setId:(long)id;
- (void) setTimeOfRequesting:(Date *)timeOfRequesting;
- (void) setTimeOfAssigning:(Date *)timeOfAssigning;
- (void) setTimeOfArriving:(Date *)timeOfArriving;
- (void) setTimeOfSeating:(Date *)timeOfSeating;
- (void) setTimeOfFinishing:(Date *)timeOfFinishing;
- (void) setTimeOfPayment:(Date *)timeOfPayment;
- (void) setExpectedArrivalTimes:(NSMutableArray *)expectedArrivalTimes;
- (void) setRating:(char)rating;
- (void) setAddressFrom:(NSString *)addressFrom;
- (void) setAddressTo:(NSString *)addressTo;
- (void) setLocationFrom:(ItemLocation *)locationFrom;
- (void) setLocationTo:(ItemLocation *)locationTo;
- (void) setPaymentType:(int)paymentType;
- (void) setPrice:(float)price;
- (void) setDriverInfo:(DriverInfo *)driverInfo;
- (void) setClientInfo:(ClientInfo *)clientInfo;
- (void) setAutoType:(int)autoType;
- (void) setComment:(NSString *)comment;
- (void) setOptions:(NSMutableArray *)options;
- (void) setFixedMission:(BOOL)fixedMission;
@end
