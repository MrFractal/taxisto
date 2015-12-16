#import "MissionInfo.h"
#import "PaymentInfo.h"

@interface PaymentResponse : NSObject {
  MissionInfo * missionInfo;
  PaymentInfo * paymentInfo;
  BOOL payment;
}

@property(nonatomic) BOOL payment;
@property(nonatomic, retain) MissionInfo * missionInfo;
@property(nonatomic, retain) PaymentInfo * paymentInfo;
@property(nonatomic) BOOL payment;
- (void) init;
- (void) setPayment:(BOOL)payment;
- (void) setMissionInfo:(MissionInfo *)missionInfo;
- (void) setPaymentInfo:(PaymentInfo *)paymentInfo;
@end
