
/**
 * Created by ivanenok on 4/14/2014.
 */

@interface RateDriverRequest : NSObject {
  long driverId;
  long missionId;
  NSString * comment;
  int totalRate;
  int rateOption1;
  int rateOption2;
  int rateOption3;
  int rateOption4;
}

@property(nonatomic) long driverId;
@property(nonatomic) long missionId;
@property(nonatomic, retain) NSString * comment;
@property(nonatomic) int totalRate;
@property(nonatomic) int rateOption1;
@property(nonatomic) int rateOption2;
@property(nonatomic) int rateOption3;
@property(nonatomic) int rateOption4;
- (void) setDriverId:(long)driverId;
- (void) setMissionId:(long)missionId;
- (void) setComment:(NSString *)comment;
- (void) setTotalRate:(int)totalRate;
- (void) setRateOption1:(int)rateOption1;
- (void) setRateOption2:(int)rateOption2;
- (void) setRateOption3:(int)rateOption3;
- (void) setRateOption4:(int)rateOption4;
@end
