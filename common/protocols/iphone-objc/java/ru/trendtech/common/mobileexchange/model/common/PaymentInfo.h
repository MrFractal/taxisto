#import "NSMutableArray.h"
#import "NSMutableArray.h"

/**
 * Created by ivanenok on 4/14/2014.
 */

@interface PaymentInfo : NSObject {
  float totalPrice;
  int missionTime;
  int pausesCount;
  int pausesTime;
  int distance;
  int waitingOver;
  int billStatus;
  NSString * comment;
  BOOL fixedPrice;
  NSMutableArray * services;
}

@property(nonatomic) float totalPrice;
@property(nonatomic) int missionTime;
@property(nonatomic) int pausesCount;
@property(nonatomic) int pausesTime;
@property(nonatomic) int distance;
@property(nonatomic) int waitingOver;
@property(nonatomic) int billStatus;
@property(nonatomic, retain) NSString * comment;
@property(nonatomic) BOOL fixedPrice;
@property(nonatomic, retain) NSMutableArray * services;
- (void) init;
- (void) setTotalPrice:(float)totalPrice;
- (void) setMissionTime:(int)missionTime;
- (void) setPausesCount:(int)pausesCount;
- (void) setPausesTime:(int)pausesTime;
- (void) setDistance:(int)distance;
- (void) setWaitingOver:(int)waitingOver;
- (void) setBillStatus:(int)billStatus;
- (void) setComment:(NSString *)comment;
- (void) setFixedPrice:(BOOL)fixedPrice;
- (void) setServices:(NSMutableArray *)services;
@end
