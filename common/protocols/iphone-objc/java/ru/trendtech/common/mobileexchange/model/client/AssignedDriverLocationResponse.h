#import "ItemLocation.h"
#import "NSMutableArray.h"
#import "NSMutableArray.h"

/**
 * Created by max on 06.02.14.
 */

@interface AssignedDriverLocationResponse : NSObject {
  ItemLocation * location;
  BOOL arrived;
  NSMutableArray * arrivalTimes;
}

@property(nonatomic, retain) ItemLocation * location;
@property(nonatomic) BOOL arrived;
@property(nonatomic, retain, readonly) NSMutableArray * arrivalTimes;
- (void) init;
- (void) setLocation:(ItemLocation *)location;
- (void) setArrived:(BOOL)arrived;
@end
