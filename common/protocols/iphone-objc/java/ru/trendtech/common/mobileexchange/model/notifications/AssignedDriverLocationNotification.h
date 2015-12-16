#import "ItemLocation.h"

/**
 * Created by max on 06.02.14.
 */

@interface AssignedDriverLocationNotification : NSObject {
  ItemLocation * loc;
  long id;
}

@property(nonatomic, retain) ItemLocation * loc;
@property(nonatomic) long id;
- (void) setLoc:(ItemLocation *)loc;
- (void) setId:(long)id;
@end
